<template>
  <view class="container">
    <uni-section title="商品信息" type="line">
      <view class="example">
        <!-- 基础用法，不包含校验规则 -->
        <uni-forms ref="baseForm" :model="baseFormData" labelWidth="80px">
          <uni-forms-item label="分类">
            <uni-data-select :localdata="categoryList" v-model="baseFormData.categoryId"></uni-data-select>
          </uni-forms-item>
          <uni-forms-item label="描述">
            <uni-easyinput v-model="baseFormData.other"/>
          </uni-forms-item>
          <uni-forms-item label="数量">
            <uni-number-box :min="1" :max="100" v-model="baseFormData.count" />
          </uni-forms-item>
          <uni-forms-item label="采购价">
            <uni-easyinput type="number" v-model="baseFormData.purchasePrice"/>
          </uni-forms-item>
          <uni-forms-item label="零售价">
            <uni-easyinput type="number" v-model="baseFormData.sellingPrice"/>
          </uni-forms-item>
          <uni-forms-item label="图片">
            <view class="uni-list list-pd">
              <view class="uni-list-cell cell-pd">
                <view class="uni-uploader">
                  <view class="uni-uploader-body">
                    <view class="uni-uploader__files">
                      <block v-for="(image,index) in imageList" :key="index">
                        <view class="uni-uploader__file">
                          <image class="uni-uploader__img" :src="image" :data-src="image" @tap="previewImage"></image>
                        </view>
                      </block>
                      <view class="uni-uploader__input-box">
                        <view class="uni-uploader__input" @tap="chooseImage"></view>
                      </view>
                    </view>
                  </view>
                </view>
              </view>
            </view>
          </uni-forms-item>
        </uni-forms>
        <view class="button-group">
          <button type="primary" @click="formSubmit">入库</button>
        </view>
      </view>
    </uni-section>
  </view>
</template>

<script>
import permision from "@/common/permission.js"
import graceChecker from "@/common/graceChecker.js"
import store from '@/store/index.js'
import UniDataChecklist from "../../uni_modules/uni-data-checkbox/components/uni-data-checkbox/uni-data-checkbox";
var sourceType = [
  ['camera'],
  ['album'],
  ['camera', 'album']
]
var sizeType = [
  ['compressed'],
  ['original'],
  ['compressed', 'original']
]
export default {
  components: {UniDataChecklist},
  data() {
    return {
      title: 'choose/previewImage',
      imageList: [],
      sourceTypeIndex: 2,
      sourceType: ['拍照', '相册', '拍照或相册'],
      sizeTypeIndex: 2,
      sizeType: ['压缩', '原图', '压缩或原图'],
      countIndex: 8,
      count: [1, 2, 3, 4, 5, 6, 7, 8, 9],
      inStock: {
        categoryId: '',
        color: '',
        other: '',
        goodsName: '',
      },
      categoryList: [],
      imageValue: '',
      baseFormData: {
        categoryId: 0,
        other: '',
        count: 5,
        purchasePrice: 0,
        sellingPrice: 0
      }
    }
  },
  onUnload() {
    this.imageList = [],
        this.sourceTypeIndex = 2,
        this.sourceType = ['拍照', '相册', '拍照或相册'],
        this.sizeTypeIndex = 2,
        this.sizeType = ['压缩', '原图', '压缩或原图'],
        this.countIndex = 8;
  },
  mounted(){
    this.loadCategory();
  },
  methods: {
    loadCategory() {
      uni.request({
        url: '/category/list',
        method: 'get',
        header: {
          'token': ''
        },
        success: (res) => {
          const resp = res.data;
          this.categoryList = resp.data.map(c=>{
            return {
              value: c.id,
              text: c.category
            }
          });
        },
        fail: function (err) {
          console.error(err);
        }
      });
    },
    formSubmit: async function () {
      var formData = this.baseFormData;
      if (this.imageValue) {
        formData.picture = this.imageValue;
      }
      const category = this.categoryList.filter(category => category.value === formData.categoryId)[0].text;
      formData.goodsName = category + '-' + formData.other;
      var checkRes = true;
      if (checkRes) {
        uni.request({
          url: '/goods',
          method: 'post',
          header: {
            'token': ''
          },
          data: formData,
          success: (res) => {
            const resp = res.data;
            if (resp.code === 10000) {
              uni.showToast({title: '保存成功', icon: 'success', duration: 500});
              this.baseFormData = {
                categoryId: 0,
                other: '',
                count: 0,
                purchasePrice: 0,
                sellingPrice: 0
              }
              this.imageList = [];
              this.imageValue = '';
            } else {
              uni.showToast({title: "失败", icon: 'error', duration: 2000});
            }
          },
          fail: function (err) {
            console.error(err);
          }
        });
      } else {
        uni.showToast({title: graceChecker.error, icon: "none"});
      }
    },
    chooseImage: async function () {
      // #ifdef APP-PLUS
      // TODO 选择相机或相册时 需要弹出actionsheet，目前无法获得是相机还是相册，在失败回调中处理
      if (this.sourceTypeIndex !== 2) {
        let status = await this.checkPermission();
        if (status !== 1) {
          return;
        }
      }
      // #endif

      if (this.imageList.length === 9) {
        let isContinue = await this.isFullImg();
        console.log("是否继续?", isContinue);
        if (!isContinue) {
          return;
        }
      }
      uni.chooseImage({
        sourceType: sourceType[this.sourceTypeIndex],
        sizeType: sizeType[this.sizeTypeIndex],
        count: this.imageList.length + this.count[this.countIndex] > 9 ? 9 - this.imageList.length : this.count[this.countIndex],
        success: (res) => {
          this.uploadFile(res.tempFilePaths);
          this.imageList = this.imageList.concat(res.tempFilePaths);
        },
        fail: (err) => {
          console.log("err: ", err);
          // #ifdef APP-PLUS
          if (err['code'] && err.code !== 0 && this.sourceTypeIndex === 2) {
            this.checkPermission(err.code);
          }
          // #endif
          // #ifdef MP
          if (err.errMsg.indexOf('cancel') !== '-1') {
            return;
          }
          uni.getSetting({
            success: (res) => {
              let authStatus = false;
              switch (this.sourceTypeIndex) {
                case 0:
                  authStatus = res.authSetting['scope.camera'];
                  break;
                case 1:
                  authStatus = res.authSetting['scope.album'];
                  break;
                case 2:
                  authStatus = res.authSetting['scope.album'] && res.authSetting['scope.camera'];
                  break;
                default:
                  break;
              }
              if (!authStatus) {
                uni.showModal({
                  title: '授权失败',
                  content: 'Hello uni-app需要从您的相机或相册获取图片，请在设置界面打开相关权限',
                  success: (res) => {
                    if (res.confirm) {
                      uni.openSetting()
                    }
                  }
                })
              }
            }
          })
          // #endif
        }
      })
    },
    uploadFile(tempFilePaths) {
      // const token = uni.getStorageSync("token")
      const token = store.state.token;
      uni.uploadFile({
        // url: 'http://localhost:8081/renren-api/sys/oss/upload',
        url: 'https://www.dodomido.cc/yy/sys/oss/upload',
        header: {
          'token': token
        },
        name: 'file',
        filePath: tempFilePaths[0],
        success: (res) => {
          const respStr = res.data;
          const resp = JSON.parse(respStr)
          if (resp.code === 10000) {
            uni.showToast({
              title: '上传成功',
              icon: 'success',
              duration: 500
            });
            this.imageValue = resp.data
          }
        },
        fail: function (err) {
          console.error(err);
        }
      });
    },
    isFullImg: function () {
      return new Promise((res) => {
        uni.showModal({
          content: "已经有9张图片了,是否清空现有图片？",
          success: (e) => {
            if (e.confirm) {
              this.imageList = [];
              res(true);
            } else {
              res(false)
            }
          },
          fail: () => {
            res(false)
          }
        })
      })
    },
    previewImage: function (e) {
      var current = e.target.dataset.src
      uni.previewImage({
        current: current,
        urls: this.imageList
      })
    },
    async checkPermission(code) {
      let type = code ? code - 1 : this.sourceTypeIndex;
      let status = permision.isIOS ? await permision.requestIOS(sourceType[type][0]) :
          await permision.requestAndroid(type === 0 ? 'android.permission.CAMERA' :
              'android.permission.READ_EXTERNAL_STORAGE');

      if (status === null || status === 1) {
        status = 1;
      } else {
        uni.showModal({
          content: "没有开启权限",
          confirmText: "设置",
          success: function (res) {
            if (res.confirm) {
              permision.gotoAppSetting();
            }
          }
        })
      }

      return status;
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
