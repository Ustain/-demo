<template>
  <div>
    <el-card style="width: 40%;margin: 10px">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" disabled></el-input>
        </el-form-item>
        <el-form-item label="角色">
          <el-input v-model="form.nickName" disabled></el-input>
        </el-form-item>
        <el-form-item label="年龄">
          <el-input v-model="form.age"></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-radio v-model="form.sex" label="男">男</el-radio>
          <el-radio v-model="form.sex" label="女">女</el-radio>
          <el-radio v-model="form.sex" label="未知">未知</el-radio>
        </el-form-item>
        <el-form-item label="电话号码" prop="tel">
          <el-input v-model="form.tel" style="width:80%"/>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" show-password></el-input>
        </el-form-item>
      </el-form>
      <div style="text-align: center">
        <el-button type="primary" @click="update">保存</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Person",
  data(){
    return{
      form:{}
    }
  },
  created() {
    let str = sessionStorage.getItem("user") || "{}";
    this.form = JSON.parse(str);
  },
  computed: {
    rules() {
      return {
        tel: [
          { required: true, message: "请输入电话号码", trigger: "blur" }
        ]
      };
    }
  },
  methods:{
    update(){
      this.$refs.form.validate(valid => {
        if (valid) {
          request.put("/api/user",this.form).then(res => {
            console.log(res);
            if (res.code === '0'){
              this.$message({
                type:"success",
                message:"更新成功"
              })
              sessionStorage.setItem("user",JSON.stringify(this.form));
              this.$router.push("/user")    //登陆成功之后进行页面的跳转跳转到主页
            }else {
              this.$message({
                type:"error",
                message:res.msg
              });
            }
          });
        }
      });
    }
  }
}
</script>

<style scoped>

</style>
