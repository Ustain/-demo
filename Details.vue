<template>

  <div>
    <el-descriptions title="详细信息">
      <el-descriptions-item label="具体位置">
        <el-tag size="small">{{ neighbourhood+"小区"+sid+"号车位"}}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="状态">
        <el-tag size="small">{{state}}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="评分">
        <el-tag size="small">{{ rate }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="电话号码">
        <el-tag size="small">{{ tel }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="价格（时租）">
        <el-tag size="small">{{ price }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="该小区车位数">
        <el-tag size="small">{{ count }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="该小区车位均价">
        <el-tag size="small">{{ avgPrice }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="浏览量">
        <el-tag size="small">{{ count2 }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item style="right: auto">
        <!-- 添加按钮 -->
        <el-button type="primary" @click="Details">查看相关评论</el-button>
      </el-descriptions-item>
    </el-descriptions>

    <el-drawer v-model="drawer" title="评论列表" direction="rtl" :custom-class="'comment-drawer'">
      <ul v-if="filteredComments.length > 0">
        <li v-for="comment in filteredComments" :key="comment.id" class="comment-item">
          <div class="comment-header">
            <span class="user-info">用户 - {{ tel }}</span>
          </div>
          <div class="comment-content">{{ comment.comments }}</div>
        </li>
      </ul>
      <div v-else>
        暂无评论
      </div>
    </el-drawer>


    <div id="mapContainer" style="width: 100%; height: 700px;"></div>


  </div>
</template>

<script>
import axios from "axios";
import request from "@/utils/request";

export default {
  created() {
    // 获取传递过来的数据
    const id = this.$route.query.id;
    const neighbourhood = this.$route.query.neighbourhood;
    const sid = this.$route.query.sid;
    const price = this.$route.query.price;
    const state = this.$route.query.state;
    const rate = this.$route.query.rate;
    const tel = this.$route.query.tel;
    const count = this.$route.query.count;
    const avgPrice = this.$route.query.avgPrice;
    const count2 = this.$route.query.count2;

    // 在控制台打印数据，检查是否正确获取到
    console.log('ID:', id);
    console.log('Neighbourhood:', neighbourhood);
    console.log('SID:', sid);

    // 将接收到的值赋给data中的字段
    this.neighbourhood = neighbourhood;
    this.sid = sid;
    this.rate = rate;
    this.price = price;
    this.state = state;
    this.tel = tel;
    this.count = count;
    this.avgPrice = avgPrice;
    this.count2 = count2;
    // 进一步处理数据，更新界面等
    // ...
  },

  data() {
    return {
      map: null,
      markers: [], // 保存所有的标记点
      neighbourhood: '', // 保存上一个界面传递过来的neighbourhood值
      sid: '', // 保存上一个界面传递过来的sid值
      tel:'',
      state:'',
      price:'',
      rate:'',
      comments:'',
      commentsList: [], // 保存评论数据
      drawer: false, // 控制侧边栏的显示与隐藏
    };
  },
  // 在 mounted() 钩子中加载地图和插件
  mounted() {
    // 初始化地图
    this.initMap();
    // 调用后端接口获取坐标点数据
    this.getGeoData();
  },
  computed: {
    filteredComments() {
      return this.commentsList.filter((comment) => {
        return (
            comment.neighbourhood === this.neighbourhood &&
            comment.sid === parseInt(this.sid) &&
            comment.comments && comment.comments.trim() !== ''
        );
      });
    },

  },

  methods: {
    Details() {
      // 调用后端接口获取评论数据
      request
          .get("/api/comments")
          .then((response) => {
            this.commentsList = response.data;
          })
          .catch((error) => {
            console.error(error);
          });
      this.drawer = true; // 显示侧边栏
    },
    initMap() {
      // 创建地图实例
      this.map = new AMap.Map("mapContainer", {
        zoom: 12, // 初始缩放级别
      });
      // 加载地理编码插件
      AMap.plugin("AMap.Geocoder", () => {
        // 地理编码插件加载完成后的回调函数
        // 可以在这里进行相关操作
      });
      // 加载自动完成插件
      AMap.plugin("AMap.AutoComplete", () => {
        // 自动完成插件加载完成后的回调函数
        // 可以在这里进行相关操作
      });
    },
    getGeoData() {
      // 调用后端接口获取坐标点数据
      request
          .get("api/geo")
          .then((response) => {
            const data = response.data;
            console.log("从后端接口获取到的数据:", data); // 打印数据到控制台，检查数据是否正确
            if (data && data.length > 0) {
              const filteredData = data.filter(item => {
                return item.neighbourhood === this.$route.query.neighbourhood && item.sid === parseInt(this.$route.query.sid);
              });
              console.log("过滤后的数据:", filteredData);

              if (filteredData.length > 0) {
                this.addMarkers([filteredData[0]]); // 添加坐标点标记
              }
            }
          })
          .catch((error) => {
            console.error(error);
          });
    },

    addMarkers(data) {
      // 添加坐标点标记到地图上
      data.forEach((item) => {
        const marker = new AMap.Marker({
          position: [item.y, item.x], // 设置标记点的经纬度
          map: this.map, // 指定地图对象
        });
        // 添加鼠标悬停事件监听器
        marker.on("mouseover", () => {
          this.showMarkerInfoWindow(item, marker);
        });
        this.markers.push(marker); // 将标记点保存到数组中
      });

      console.log("收到的点数据:", data); // 打印数据到控制台
    },

    showMarkerInfoWindow(item, marker) {
      // 创建信息窗体内容
      const content = `
    <div>
      <div><strong>小区:</strong> ${item.neighbourhood}</div>
      <div><strong>车位号:</strong> ${item.sid}</div>
    </div>
  `;

      // 创建信息窗体对象
      const infoWindow = new AMap.InfoWindow({
        content: content, // 设置信息窗体内容
      });

      // 打开信息窗体
      infoWindow.open(this.map, marker.getPosition());
    },

  },
};
</script>

<style>
#mapContainer {
  width: 100%;
  height: 100%;
}

.comment-drawer {
  background-color: #f5f5f5;
  font-size: 14px;
}

.comment-item {
  margin-bottom: 10px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.comment-header {
  font-weight: bold;
}

.user-info {
  color: #888;
}

.comment-content {
  margin-top: 5px;
}
</style>
