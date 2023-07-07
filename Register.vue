<template>
  <div style="width: 100%; height: 100vh; background-color: darkslategray; overflow: hidden">
    <div style="width: 400px; margin: 150px auto">
      <div style="color: #cccccc; font-size: 30px; text-align: center; padding: 30px;">欢迎注册</div>
      <el-form ref="form" :model="form" :rules="rules">
        <el-form-item prop="username">
          <el-input prefix-icon="userFilled" placeholder="账号" v-model="form.username"></el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input prefix-icon="lock" placeholder="密码" v-model="form.password" show-password></el-input>
        </el-form-item>

        <el-form-item prop="confirm">
          <el-input prefix-icon="lock" placeholder="确认密码" v-model="form.confirm" show-password></el-input>
        </el-form-item>

        <el-form-item>
          <el-button style="width: 100%" type="primary" @click="register">注  册</el-button>
        </el-form-item>

        <el-form-item>
          <el-button style="width: 100%" type="primary" @click="toLogin">返  回</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Login",
  data() {
    return {
      form: {}
    }
  },
  methods: {
    toLogin(){
      this.$router.push("/Login")
    },
    register() {
      this.$refs.form.validate(valid => {
        if (valid) {
          if (this.form.password !== this.form.confirm) {
            this.$message({
              type: "error",
              message: '两次密码输入不一致，请再次确认'
            })
            return
          }

          request.post("/api/user/register", this.form).then(res => {
            if (res.code === '0') {
              this.$message({
                type: "success",
                message: "注册成功"
              })
              this.$router.push("/login")    //注册成功之后路由到登陆界面
            } else {
              this.$message({
                type: "error",
                message: res.msg
              })
            }
          })
        }
      })
    }
  },
  computed: {
    rules() {
      return {
        username: [
          { required: true, message: '请输入账号', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ],
        confirm: [
          { required: true, message: '请再次输入密码', trigger: 'blur' }
        ]
      }
    }
  }
}
</script>

<style scoped>

</style>
