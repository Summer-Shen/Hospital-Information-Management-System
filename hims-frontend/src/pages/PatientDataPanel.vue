<template>
  <el-container>
    <el-header><navmenu></navmenu></el-header>
    <el-main>
      <el-row type="flex" justify="center">
        <el-col align="left">
          <el-page-header
            @back="goBack"
            :content="this.worker.name + '所负责的病人'"
          >
          </el-page-header>
        </el-col>
      </el-row>
      <el-row type="flex" justify="center">
        <el-col type="flex" justify="center">
          <el-table :data="tableData" align="center" empty-text="暂无数据">
            <el-table-column prop="id" label="ID" width="120">
            </el-table-column>
            <el-table-column prop="name" label="姓名" width="150">
            </el-table-column>
            <el-table-column prop="age" label="年龄" width="120">
            </el-table-column>
            <el-table-column
              prop="t_area_id"
              label="所在病区"
              width="120"
              :filters="[
                { text: '1', value: 1 },
                { text: '2', value: 2 },
                { text: '3', value: 3 },
                { text: '隔离区', value: '隔离区' },
              ]"
              :filter-method="filterArea"
              filter-placement="bottom-end"
            >
            </el-table-column>
            <el-table-column
              prop="rating"
              label="病情评级"
              width="120"
              :filters="[
                { text: '轻症', value: '轻症' },
                { text: '重症', value: '重症' },
                { text: '危重症', value: '危重症' },
              ]"
              :filter-method="filterRating"
              filter-placement="bottom-end"
            >
            </el-table-column>
            <el-table-column
              prop="state"
              label="生命状态"
              width="120"
              :filters="[
                { text: '康复出院', value: '康复出院' },
                { text: '在院治疗', value: '在院治疗' },
                { text: '病亡', value: '病亡' },
                { text: '隔离区', value: '隔离区' },
              ]"
              :filter-method="filterState"
              filter-placement="bottom-end"
            >
            </el-table-column>
            <el-table-column
              prop="is_to_be_released"
              label="待出院"
              width="120"
              :filters="[
                { text: '是', value: '是' },
                { text: '否', value: '否' },
              ]"
              :filter-method="filterReleased"
              filter-placement="bottom-end"
            >
            </el-table-column>
            <el-table-column
              prop="is_to_be_transferred"
              label="待转区"
              width="120"
              :filters="[
                { text: '是', value: '是' },
                { text: '否', value: '否' },
              ]"
              :filter-method="filterTransferred"
              filter-placement="bottom-end"
            >
            </el-table-column>
            <el-table-column align="right">
              <template slot="header" slot-scope="scope">
                <el-button
                  size="mini"
                  @click="handleAdd(scope.$index, scope.row)"
                  v-if="isENurse"
                  >添加</el-button
                >
              </template>
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  @click="handleEdit(scope.$index, scope.row)"
                  >详情</el-button
                >

                <el-button
                  size="mini"
                  @click="handleNat(scope.$index, scope.row)"
                  >查看核酸检测单</el-button
                >

                <el-button
                  size="mini"
                  @click="handleDr(scope.$index, scope.row)"
                  >查看每日信息</el-button
                >
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>

<script>
import navmenu from "../components/Nav.vue";

export default {
  name: "PatientDataPanel",
  components: { navmenu },
  data() {
    return {
      user: [],
      tableData: [],
      isENurse: false,
      loading: false,

      worker: {
        id: "",
        u_type: "",
        name: "",
      },
    };
  },
  created() {
    this.handleUserData();
    this.loadTableData();
  },
  methods: {
    handleUserData() {
      if (this.$store.state.user) {
        this.user = this.$store.state.user;
        this.isENurse = this.user.u_type == "e_nurse";
      }

      if (this.$route.params.w_id && this.$route.params.w_name) {
        this.worker.id = this.$route.params.w_id;
        this.worker.name = this.$route.params.w_name;
        this.worker.u_type = "w_nurse";
      } else {
        this.worker.id = this.user.id;
        this.worker.name = "我";
        this.worker.u_type = this.user.u_type;
      }
    },
    loadTableData() {
      this.$axios
        .get("/patientDataPanel", {
          params: { id: this.worker.id, type: this.worker.u_type },
        })
        .then((resp) => {
          if (resp.status === 200) {
            var index = -1;
            resp.data.patient.forEach((element) => {
              index++;
              this.loadRow(index, element);
            });
          } else {
            this.$message.error("请求错误，请重试");
          }
        })
        .catch((error) => {
          console.log(error);
          this.$message.error("请求错误，请重试");
        });
    },

    loadRow(index, patient) {
      this.tableData.push({
        id: patient.id,
        name: patient.name,
        age: patient.age,
        t_area_id: patient.t_area_id == 0 ? "隔离区" : patient.t_area_id,
        rating: this.parseRating(patient.rating),
        state: this.parseState(patient.state),
        is_to_be_released: this.parseReleased(patient.is_to_be_released),
        is_to_be_transferred: this.parseTransferred(
          patient.is_to_be_transferred
        ),
      });
    },
    parseRating(rating) {
      switch (rating) {
        case "mild":
          return "轻症";
        case "severe":
          return "重症";
        case "critical":
          return "危重症";
      }
    },
    parseState(state) {
      switch (state) {
        case "discharge":
          return "康复出院";
        case "hospitalized":
          return "在院治疗";
        case "dead":
          return "病亡";
        default:
          return "隔离区";
      }
    },
    parseReleased(is_to_be_released) {
      switch (is_to_be_released) {
        case true:
          return "是";
        case false:
          return "否";
      }
    },
    parseTransferred(is_to_be_transferred) {
      switch (is_to_be_transferred) {
        case true:
          return "是";
        case false:
          return "否";
      }
    },

    filterArea(value, row) {
      return row.t_area_id === value;
    },
    filterRating(value, row) {
      return row.rating === value;
    },
    filterState(value, row) {
      return row.state === value;
    },
    filterReleased(value, row) {
      return row.is_to_be_released === value;
    },
    filterTransferred(value, row) {
      return row.is_to_be_transferred === value;
    },

    goBack() {
      this.$router.push("/workerDataPanel");
    },
    handleAdd(index, row) {
      this.$router.push("/patientInfo");
    },
    handleEdit(index, row) {
      this.$router.push("/patientInfo/" + row.id);
    },
    handleNat(index, row) {
      this.$router.push("/natDataPanel/" + row.id + "&" + row.name);
    },
    handleDr(index, row) {
      this.$router.push("/drDataPanel/" + row.id + "&" + row.name);
    },
  },
};
</script>

<style>
</style>