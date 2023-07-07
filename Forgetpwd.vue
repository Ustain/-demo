<template>
  <div style="width: 100%;height:100vh;background-color: darkslategray;overflow: hidden">
    <div style="width:400px;margin: 150px auto">
      <div style="color: #cccccc;font-size:30px;text-align: center;padding: 30px;">密码找回</div>
      <el-form ref="form" :model="form">
        <el-form-item>
          <el-input prefix-icon="userFilled" placeholder="请输入账号" v-model="form.username"></el-input>
        </el-form-item>

        <el-form-item>
          <el-input v-model="password" placeholder="正在找回密码..." readonly></el-input>
        </el-form-item>

        <el-form-item>
          <el-button style="width:100%" type="primary" @click="forgetpwd">确认找回</el-button>
        </el-form-item>

        <el-form-item>
          <el-button style="width:100%" type="primary" @click="toLogin">返回</el-button>
        </el-form-item>

      </el-form>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Forgetpwd",
  data() {
    return {
      form: {
        username: ""
      },
      password: ""
    };
  },
  methods: {
    toLogin() {
      this.$router.push("/Login");
    },
    forgetpwd() {
      request.post("/api/user/forgetpwd", this.form)
          .then(res => {
            if (res.code === "0") {
              this.$message({
                type: "success",
                message: "找回密码成功",
              });
              this.password = res.data; // 获取返回的密码并赋值给 password 变量
            } else {
              this.$message({
                type: "error",
                message: res.msg,
              });
            }
          })
          .catch(error => {
            console.error(error);
            this.$message({
              type: "error",
              message: "找回密码失败",
            });
          });
    }
  }
};
</script>

<style scoped>
</style>
