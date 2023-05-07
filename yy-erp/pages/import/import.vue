<template>
  <view class="container">
    <uni-section title="商品信息" type="line">
      <view class="example">
        <!-- 基础用法，不包含校验规则 -->
        <uni-forms ref="baseForm" :model="baseFormData" labelWidth="80px">
          <uni-forms-item label="条码">
            <uni-easyinput suffixIcon="scan" @change="loadGoodsInfo" v-model="baseFormData.barCode" placeholder="条码"
                           @iconClick="scanBarcode">
            </uni-easyinput>
          </uni-forms-item>
          <uni-forms-item label="名称">
            <uni-easyinput v-model="baseFormData.goodsName"/>
          </uni-forms-item>
          <uni-forms-item label="图片">
            <image class="image" style="height: 100px;" mode="aspectFill" :src="baseFormData.picture"/>
          </uni-forms-item>
          <uni-forms-item label="售价">
            <uni-easyinput type="number" v-model="baseFormData.sellingPrice"/>
          </uni-forms-item>
          <uni-forms-item label="数量">
            <uni-easyinput type="number" v-model="baseFormData.amount"/>
          </uni-forms-item>
        </uni-forms>
        <view class="button-group">
          <button type="primary" size="mini" @click="submit">入库</button>
        </view>
      </view>
    </uni-section>
  </view>
</template>

<script>

export default {
  data() {
    return {
      baseFormData: {
        barCode: '',
        goodsName: '',
        sellingPrice: 0,
        picture: '',
        amount: 5
      }
    }
  },
  methods: {
    loadGoodsInfo(barCode) {
      if (!barCode) {
        return;
      }
      uni.request({
        url: `/goods/info/${barCode}/1`,
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
          }
        },
        fail: function (err) {
          console.error(err);
        }
      });
    },
    submit() {
      uni.request({
        url: `/shop-inventory/import`,
        method: 'post',
        header: {
          'token': ''
        },
        data: this.baseFormData,
        success: (res) => {
          const resp = res.data;
          if (resp.code === 10000) {
            uni.showToast({title: '入库成功', icon: 'success', duration: 500});
            this.baseFormData.goodsName = "";
            this.baseFormData.barCode = "";
            this.baseFormData.sellingPrice = 0;
            this.baseFormData.picture = "";
            this.baseFormData.amount = 0;
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