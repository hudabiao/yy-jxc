<template>
  <view>
    <view v-if="!sysUser" class="container">
      <uni-section title="基本用法" type="line">
        <view class="example">
          <!-- 基础用法，不包含校验规则 -->
          <uni-forms ref="baseForm" :model="baseFormData" labelWidth="80px">
            <uni-forms-item label="条码">
              <uni-easyinput suffixIcon="scan" @change="loadGoodsInfo" v-model="baseFormData.barCode" placeholder="条码"
                             @iconClick="scanBarcode">
              </uni-easyinput>
            </uni-forms-item>
            <uni-forms-item label="名称">
              <uni-easyinput disabled v-model="baseFormData.goodsName"/>
            </uni-forms-item>
            <uni-forms-item label="图片">
              <image class="image" style="height: 100px;" mode="aspectFill" :src="baseFormData.picture"/>
            </uni-forms-item>
            <uni-forms-item label="库存">
              <uni-easyinput type="number" disabled v-model="baseFormData.count"/>
            </uni-forms-item>
            <uni-forms-item label="售价">
              <uni-easyinput type="number" v-model="baseFormData.sellingPrice"/>
            </uni-forms-item>
            <uni-forms-item label="出库数量">
              <uni-easyinput type="number" v-model="baseFormData.sellCount"/>
            </uni-forms-item>
            <uni-forms-item label="支付方式">
              <uni-data-checkbox v-model="baseFormData.paymentMethod" :localdata="paymentMethodList"></uni-data-checkbox>
            </uni-forms-item>
          </uni-forms>
          <view class="button-group">
            <button type="primary" @click="submit">确认销售</button>
          </view>
        </view>
      </uni-section>
    </view>
    <view v-else>
      此页面是”店铺销售页“，管理账户不展示
    </view>
  </view>

</template>

<script>
import { mapState } from 'vuex'
export default {
  data() {
    return {
      baseFormData: {
        barCode: '',
        goodsName: '',
        sellingPrice: 0,
        picture: '',
        sellCount: 1,
        count: 0,
        paymentMethod: 1
      },
      paymentMethodList: [
        {text: '微信', value: 1},
        {text: '支付宝', value: 2},
        {text: '现金', value: 3},
        {text: '其他', value: 4}
      ],
      sysUser: false
    }
  },
  computed: {
    ...mapState({
      menuList: state => state.menuList
    })
  },
  onLoad(){
    const index = this.menuList.findIndex(menu=>{
      return menu.url === '/pages/inventory/index'
    });
    if(index>=0){
      this.sysUser=true
    }
  },
  methods: {
    loadGoodsInfo(barCode) {
      if (!barCode) {
        return;
      }
      uni.request({
        url: `/goods/info/${barCode}/2`,
        method: 'get',
        header: {
          'token': '4df1d2cfdf0a27b13aeab6ec40de5ac7'
        },
        success: (res) => {
          const resp = res.data;
          if (resp.code === 10000) {
            this.baseFormData.goodsName = resp.data.goodsName;
            this.baseFormData.sellingPrice = resp.data.sellingPrice;
            this.baseFormData.picture = resp.data.picture;
            this.baseFormData.count = resp.data.count;
          } else {
            uni.showToast({title: "条码错误！", icon: 'error', duration: 2000});
          }
        },
        fail: function (err) {
          uni.showToast({title: "出错！", icon: 'error', duration: 2000});
          console.error(err);
        }
      });
    },
    submit() {
      uni.request({
        url: `/shop-inventory/sell`,
        method: 'post',
        header: {
          'token': '4df1d2cfdf0a27b13aeab6ec40de5ac7'
        },
        data: this.baseFormData,
        success: (res) => {
          const resp = res.data;
          if (resp.code === 10000) {
            uni.showToast({title: '出库成功', icon: 'success', duration: 500});
            this.baseFormData.barCode = "";
            this.baseFormData.goodsName = "";
            this.baseFormData.sellingPrice = 0;
            this.baseFormData.picture = "";
            this.baseFormData.count = 0;
          }
        },
        fail: function (err) {
          console.error(err);
        }
      });
    },
    async scanBarcode() {
      uni.scanCode({
        success: (res) => {
          console.log("扫码结果", res.result)
          this.loadGoodsInfo(res.result);
          this.baseFormData.barCode = res.result
        },
        fail: (err) => {
          // 需要注意的是小程序扫码不需要申请相机权限
        }
      });
    }
  }
}
</script>

<style lang="scss">
.container {
  /* #ifndef APP-NVUE */
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  /* #endif */
}

.example {
  padding: 15px;
  background-color: #fff;
}
</style>