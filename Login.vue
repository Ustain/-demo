<template>
<div style="width: 100%;height:100vh;background-color: darkslategray;overflow: hidden">
  <div style="width:400px;margin: 150px auto">
    <div style="color: #cccccc;font-size:30px;text-align: center;padding: 30px;">欢迎登录</div>
    <el-form ref="form" :model="form">
      <el-form-item>
        <el-input  prefix-icon="userFilled" placeholder="账号" v-model="form.username">

        </el-input>
      </el-form-item>

      <el-form-item>
        <el-input  prefix-icon="lock"  placeholder="密码" v-model="form.password" show-password>

        </el-input>
      </el-form-item>

      <el-form-item>
        <el-button style="width:100%" type="primary" @click="login">
          登  录
        </el-button>
      </el-form-item>

      <el-form-item>
        <el-button style="width:100%" type="primary" @click="toRegister">
          注  册
        </el-button>
      </el-form-item>

      <el-form-item>
        <el-link style="width:100%" type="primary" @click="toForgetpwd">
          忘记密码？
        </el-link>
      </el-form-item>
    </el-form>
  </div>
</div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Login",
  data(){
    return{
      form:{}
    }
  },
  methods:{
    toRegister(){
      this.$router.push("/register")
    },
    toForgetpwd(){
      this.$router.push("/forgetpwd")
    },
    created() {
      sessionStorage.removeItem("user");
    },
    login(){
      request.post("/api/user/login",this.form).then(res=>{
        if(res.code==='0'){
          this.$message({
            type:"success",
            message:"登陆成功"
          })
          sessionStorage.setItem("user",JSON.stringify(res.data));//缓存用户信息
          this.$router.push("/person")    //登陆成功之后进行页面的跳转跳转到主页
        }else{
          this.$message({
            type:"error",
            message:res.msg
          })
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
