<script>
import {mapMutations} from 'vuex'
import {version} from './package.json'
import store from '@/store/index.js'

Date.prototype.format = function (format) {
  var date = {
    "M+": this.getMonth() + 1,
    "d+": this.getDate(),
    "h+": this.getHours(),
    "m+": this.getMinutes(),
    "s+": this.getSeconds(),
    "q+": Math.floor((this.getMonth() + 3) / 3),
    "S+": this.getMilliseconds()
  };
  if (/(y+)/i.test(format)) {
    format = format.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length));
  }
  for (var k in date) {
    if (new RegExp("(" + k + ")").test(format)) {
      format = format.replace(RegExp.$1, RegExp.$1.length == 1 ?
          date[k] : ("00" + date[k]).substr(("" + date[k]).length));
    }
  }
  return format;
}

export default {
  onLaunch: function () {
    // 注册-请求拦截器
    uni.addInterceptor('request', {
      invoke(args) {
        const token = store.state.token;
        console.log('token', token);
        if (token) {
          args.header.token = token;
        }
        // args.url = 'http://localhost:8081/renren-api' + args.url;
        args.url = 'https://www.dodomido.cc/yy' + args.url;
      },
      fail(err) {
        console.log('interceptor-fail', err)
      },
      complete(res) {
        console.log('interceptor-complete', res)
        const resp = res.data;
        if (resp.code === 401) {
          uni.redirectTo({
            url: "../login/login",
          })
        }
      }
    });

    // #ifdef H5
    console.log(
        `%c hello uniapp %c v${version} `,
        'background:#35495e ; padding: 1px; border-radius: 3px 0 0 3px;  color: #fff',
        'background:#007aff ;padding: 1px; border-radius: 0 3px 3px 0;  color: #fff; font-weight: bold;'
    )
    // #endif
    console.log('App Launch');
  },
  onShow: function () {
    console.log('App Show')
  },
  onHide: function () {
    console.log('App Hide')
  },
  globalData: {
    test: ''
  },
  methods: {
    ...mapMutations(['setUniverifyErrorMsg', 'setUniverifyLogin'])
  }
}
</script>

<style>
/* #ifndef APP-PLUS-NVUE */
/* uni.css - 通用组件、模板样式库，可以当作一套ui库应用 */
@import './common/uni.css';

/* H5 兼容 pc 所需 */
/* #ifdef H5 */
@media screen and (min-width: 768px) {
  body {
    overflow-y: scroll;
  }
}

/* 顶栏通栏样式 */
/* .uni-top-window {
    left: 0;
    right: 0;
} */

uni-page-body {
  background-color: #F5F5F5 !important;
  min-height: 100% !important;
  height: auto !important;
}

.uni-top-window uni-tabbar .uni-tabbar {
  background-color: #fff !important;
}

.uni-app--showleftwindow .hideOnPc {
  display: none !important;
}

/* #endif */

/* 以下样式用于 hello uni-app 演示所需 */
page {
  background-color: #efeff4;
  height: 100%;
  font-size: 28 rpx;
  /* line-height: 1.8; */
}

.fix-pc-padding {
  padding: 0 50px;
}

.uni-header-logo {
  padding: 30 rpx;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin-top: 10 rpx;
}

.uni-header-image {
  width: 100px;
  height: 100px;
}

.uni-hello-text {
  color: #7A7E83;
}

.uni-hello-addfile {
  text-align: center;
  line-height: 300 rpx;
  background: #FFF;
  padding: 50 rpx;
  margin-top: 10px;
  font-size: 38 rpx;
  color: #808080;
}

/* #endif*/
</style>
