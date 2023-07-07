<template>
  <div>
    <el-menu
        style="width:200px;min-height:calc(100vh - 50px)"
        :default-active="path"

        router
        class="el-menu-vertical-demo">

      <el-sub-menu index="/1">
        <template #title>系统管理</template>
        <el-menu-item index="/user" v-if="user.nickName==='管理员'">用户信息管理</el-menu-item>
        <el-menu-item index="/info2" v-if="user.nickName==='管理员'">车位信息管理</el-menu-item>
<!--        <el-menu-item index="/book">预定信息管理</el-menu-item>-->
      </el-sub-menu>

<!--      <el-menu-item index="/parkinginfo" >-->
<!--        <el-icon>-->
<!--          <icon-menu/>-->
<!--        </el-icon>-->
<!--        <span>车位信息详情</span>-->
<!--      </el-menu-item>-->

<!--      <el-menu-item index="/shareparking" >-->
<!--        <el-icon>-->
<!--          <icon-menu/>-->
<!--        </el-icon>-->
<!--        <span>发布车位</span>-->
<!--      </el-menu-item>-->

<!--      <el-menu-item index="/order" >-->
<!--        <el-icon>-->
<!--          <icon-menu/>-->
<!--        </el-icon>-->
<!--        <span>预定车位</span>-->
<!--      </el-menu-item>-->


      <el-menu-item index="/info" >
        <el-icon>
          <icon-menu/>
        </el-icon>
        <span>车位信息详情</span>
      </el-menu-item>

      <el-menu-item index="/share" >
        <el-icon>
          <icon-menu/>
        </el-icon>
        <span>发布车位</span>
      </el-menu-item>

      <el-menu-item index="/order2" >
        <el-icon>
          <icon-menu/>
        </el-icon>
        <span>预定车位</span>
      </el-menu-item>


      <el-menu-item index="/search" >
        <el-icon >
          <document/>
        </el-icon>
        <span>榜单</span>
      </el-menu-item>


      <el-menu-item index="/Search2">
        <el-icon>
          <setting/>
        </el-icon>
        <span>搜索</span>
      </el-menu-item>

    </el-menu>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Aside",
  data(){
    return{
      user:{},
      path:this.$route.path,
    }
  },
  created() {
    let userStr =sessionStorage.getItem("user") || '{}';
    this.user = JSON.parse(userStr);
// //请求后端服务器，确认当前登录用户的合法性防止修改缓存
//     request.get("/api/user/"+this.user.id).then(res=>{
//       if (res.code==='0'){
//         this.user = res.data;
//       }
//     });
  },
  methods:{

  }
}

</script>


<style scoped>

</style>
