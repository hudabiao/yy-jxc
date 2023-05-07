<template>
  <view>
    <uni-row class="demo-uni-row">
      <uni-col :span="12">
            <uni-data-select :localdata="deptList" v-model="shopId"></uni-data-select>
      </uni-col>
      <uni-col :span="6">
        <button @click="transfer" size="mini">调拨</button>
      </uni-col>
      <uni-col :span="6">
        <button @click="exportCode" size="mini">导出条码</button>
      </uni-col>
    </uni-row>
    <uni-easyinput suffixIcon="scan" @change="confirmCode" v-model="barCode" placeholder="条码"
                   @iconClick="scanBarcode">
    </uni-easyinput>
    <uni-list>
      <uni-list-chat v-for="(item, index) in transferList"
                     :title="item.goodsName"
                     :clickable="true"
                     @click="showImage(index)"
                     :note="item.barCode"
                     :avatar="item.picture">
        <view class="chat-custom-right">
          <uni-number-box :min="1" :max="100" v-model="item.count"/>
          <button size="mini" type="primary" @click.stop="remove(index)"> 移 除</button>
        </view>
      </uni-list-chat>
    </uni-list>
  </view>
</template>

<script>
// import store from '@/store/index.js'
export default {
  data() {
    return {
      barCode: '',
      transferList: [],
      imageList: [],
      deptList: [],
      shopId: ''
    }
  },
  mounted() {
    this.loadDept();
  },
  methods: {
    loadDept() {
      uni.request({
        url: '/sys/dept/list/1067246875800000066',
        method: 'get',
        header: {
          'token': ''
        },
        success: (res) => {
          const resp = res.data;
          this.deptList = resp.data.map(c => {
            return {
              value: c.id,
              text: c.name
            }
          });
        },
        fail: function (err) {
          console.error(err);
        }
      });
    },
    exportCode() {
      // const token = store.state.token;
      uni.showLoading({
        title: '加载中'
      });
      const transferList = this.transferList.map(goods => {
        return {
          goodsId: goods.goodsId,
          amount: goods.count
        }
      })
      uni.request({
        url: '/goods/export/post',
        method: 'post',
        data: transferList,
        header: {
          'token': ''
        },
        success: (res) => {
          const resp = res.data;
          const fileName = resp.data;
          this.getDownloadUrl(fileName);
        },
        fail: function (err) {
          uni.hideLoading();
          console.error(err);
        }
      });
    },
    getDownloadUrl(fileName){
      uni.request({
        url: `/goods/getExportFileUrl/${fileName}`,
        method: 'get',
        header: {
          'token': ''
        },
        success: (res) => {
          const resp = res.data;
          const url = resp.data;
          this.downloadFile(url);
        },
        fail: function (err) {
          uni.hideLoading();
          console.error(err);
        }
      });
    },
    downloadFile(url){
      uni.downloadFile({
        url: url,
        success: (res) => {
          console.log('res', res);
          if (res.statusCode === 200) {
            uni.saveFile({
              tempFilePath: res.tempFilePath,
              success: function (res) {
                uni.hideLoading();
                var savedFilePath = res.savedFilePath;
                console.log('savedFilePath', savedFilePath);
                uni.openDocument({
                  filePath: savedFilePath,
                  showMenu: true,
                  success: function (res) {
                    console.log('打开文档成功');
                  },
                  fail: function (res) {
                    uni.showToast({title: "打开文件失败：" + filepathss, icon: 'error', duration: 2000});
                    console.log(res);
                  }
                });
              }
            });
          }
        }
      });
    },
    transfer() {
      if (!this.shopId) {
        uni.showToast({
          title: '未选择店铺',
          icon: 'error',
          duration: 2000
        });
        return;
      }
      if (this.transferList.length === 0) {
        uni.showToast({
          title: '未选择商品',
          icon: 'error',
          duration: 2000
        });
        return;
      }
      const _this = this;
      uni.showModal({
        title: '提示',
        content: '确认调拨以下商品吗？',
        success: function (res) {
          if (res.confirm) {
            _this.transferData();
          }
        }
      });
    },
    transferData() {
      const shopId = this.shopId;
      const transferList = this.transferList.map(goods => {
        return {
          goodsId: goods.goodsId,
          shopId: shopId,
          amount: goods.count
        }
      })
      uni.request({
        url: '/shop-inventory/transfer',
        method: 'post',
        header: {
          'token': ''
        },
        data: transferList,
        success: (res) => {
          const resp = res.data;
          if (resp.code === 10000) {
            uni.showToast({title: '调拨成功', icon: 'success', duration: 500});
            this.transferList = [];
            this.shopId = '';
          } else {
            uni.showToast({title: "失败", icon: 'error', duration: 2000});
          }
        },
        fail: function (err) {
          console.error(err);
        }
      });
    },
    showImage(index) {
      // console.log(index)
      uni.previewImage({
        current: index,
        urls: this.imageList
      })
    },
    async scanBarcode() {
      uni.scanCode({
        success: (res) => {
          this.barCode = res.result
          this.confirmCode(this.barCode);
        },
        fail: (err) => {
          // 需要注意的是小程序扫码不需要申请相机权限
        }
      });
    },
    remove(index) {
      this.transferList.splice(index, 1);
    },
    confirmCode(barCode) {
      // const barCode = this.barCode;
      if (!barCode) {
        return;
      }
      uni.request({
        url: `/goods/info/${barCode}/1`,
        method: 'get',
        header: {
          'token': ''
        },
        success: (res) => {
          const resp = res.data;
          if (resp.code === 10000) {
            const goodsInfo = {
              goodsId: resp.data.goodsId,
              goodsName: resp.data.goodsName,
              picture: resp.data.picture,
              category: resp.data.category,
              count: 5
            }
            this.transferList.push(goodsInfo)
            this.imageList.push(resp.data.picture)
          }
        },
        fail: function (err) {
          console.error(err);
        }
      });
    }
  }
}
</script>

<style>

</style>
