<template>
  <div style="height:50px;line-height:50px;border-bottom: 1px solid #ccc;display:flex">

    <div style="width:50%;padding-left: 30px;font-weight:bold;color:dodgerblue">私家车位共享平台--{{user.nickName}}</div>
    <div style="flex: 1"></div>
    <div style="width:100px;margin-top: 20px">
      <el-dropdown>
        <span class="el-dropdown-link">
          {{user.nickName}}<el-icon class="el-icon-arrow-down el-icon--right"></el-icon>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="$router.push('/person')">个人信息</el-dropdown-item>
            <el-dropdown-item @click="$router.push('/login')">退出系统</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
export default {
  name: "Header",
  data(){
    return{
      user:{},
    }
  },
  created() {
    let str = sessionStorage.getItem("user") || "{}";
    this.user = JSON.parse(str);
  },
  methods:{

  }
}
</script>

<style scoped>

</style>
