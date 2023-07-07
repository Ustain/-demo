<template>

  <div>
    <el-descriptions title="详细信息">
      <el-descriptions-item label="具体位置">
        <el-tag size="small">{{ neighbourhood+"小区"+sid+"号车位"}}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="电话号码">
        <el-tag size="small">{{ tel }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="租车位日期">
        <el-tag size="small">{{ date }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="使用时间段">
        <el-tag size="small">{{ startTime +"到"+ endTime}}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="预定次数">
        <el-tag size="small">{{nums }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item style="right: auto">
        <!-- 添加按钮 -->
        <el-button type="primary" @click="handleEdit">评分和评论</el-button>
      </el-descriptions-item>
    </el-descriptions>

     <!--    弹出对话框-->
    <el-dialog v-model="dialogVisible" title="Tips" width="30%">
      <el-form :model="form" label-width="120px">

        <el-form-item label="评分" class="flex">
          <div class="rate-container">
            <el-rate
                size="large"
                v-model="form.rate"
                show-score
                text-color="#ff9900"
                score-template="{value} 分"
                clearable
                allow-half
            />
          </div>
        </el-form-item>


        <el-form-item label="评论">
          <el-input  type="textarea" v-model="form.comments" style="width:100%"/>  <!--还有问题，form.sid不是最终的路径-->
        </el-form-item>

      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">
          确 认
        </el-button>
      </span>
      </template>
    </el-dialog>

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
    const date = this.$route.query.date;
    const startTime = this.$route.query.startTime;
    const endTime = this.$route.query.endTime;
    const tel = this.$route.query.tel;

    // 在控制台打印数据，检查是否正确获取到
    console.log('ID:', id);
    console.log('Neighbourhood:', neighbourhood);
    console.log('SID:', sid);

    // 将接收到的值赋给data中的字段
    this.neighbourhood = neighbourhood;
    this.sid = sid;
    this.date = date;
    this.startTime =startTime ;
    this.endTime = endTime;
    this.tel = tel;

    //nums表中获取数据
    // 在 created 钩子中调用后端接口获取 nums 信息
    this.getNumsData();
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
      nums:'',
      form:{},
      dialogVisible: false,
    };
  },
  // 在 mounted() 钩子中加载地图和插件
  mounted() {
    // 初始化地图
    this.initMap();
    // 调用后端接口获取坐标点数据
    this.getGeoData();
  },
  methods: {
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
    handleEdit(){
      this.dialogVisible = true;
      this.form = {};
    },

    save() {
      // 发送请求保存数据到后台
      const ratePayload = {
        neighbourhood: this.neighbourhood,
        sid: this.sid,
        rate: this.form.rate,
      };

      const commentPayload = {
        neighbourhood: this.neighbourhood,
        sid: this.sid,
        comments: this.form.comments,
      };

      // 使用 request 发送 POST 请求到 /api/rate
      request
          .post("/api/rate", ratePayload)
          .then((response) => {
            console.log("评分保存成功:", response.data);
            // 发送第二个请求，将评论保存到 /api/comments
            request
                .post("/api/comments", commentPayload)
                .then((response) => {
                  console.log("评论保存成功:", response.data);
                  this.dialogVisible = false; // 关闭 dialog
                  this.$message.success("发布成功"); // 显示成功提示
                })
                .catch((error) => {
                  console.error("评论保存失败:", error);
                  this.$message.error("发布失败"); // 显示失败提示
                });
          })
          .catch((error) => {
            console.error("评分保存失败:", error);
            this.$message.error("发布失败"); // 显示失败提示
          });
    },


    getNumsData() {
      // 调用后端接口获取 nums 信息
      request
          .get("/api/nums", {
            params: {
              neighbourhood: this.neighbourhood,
              sid: this.sid
            }
          })
          .then((response) => {
            const data = response.data;
            console.log("从后端接口获取到的 nums 数据:", data); // 打印数据到控制台，检查数据是否正确
            this.nums = data.nums; // 将接口返回的 nums 数据赋值给 nums 变量
          })
          .catch((error) => {
            console.error(error);
          });
    },

  },
};
</script>

<style>
#mapContainer {
  width: 100%;
  height: 100%;
}

.rate-container {
  margin-top: -8px; /* 调整为适当的负值以实现上移效果 */
}

</style>
