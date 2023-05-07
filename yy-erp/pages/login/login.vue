<template>
  <view class="login-content">
    <view class="login-title">
      登录
    </view>
    <view class="iphone">
      <input placeholder="输入手机号" :value="iphoneValue" @input="clearInput"/>
    </view>
    <view class="password" v-if="type==2">
      <input placeholder="请输入密码" v-model="passwordValue" :password="showPassword"/>
    </view>
    <view style="display: flex;justify-content: center;">
      <view class="login-btn" @click="Login()">普通登录</view>
      <view class="login-btn" @click="weixinLogin()">微信登录</view>
    </view>
  </view>
</template>

<script>
import store from '@/store/index.js'
export default {
  data() {
    return {
      iphoneValue: '', //手机号码
      passwordValue: '', //密码
      testValue: '', //验证码
      showPassword: true, //是否显示密码
      showClearIcon: false, //是否显示清除按钮
      type: 2, //登录的状态 - - - 1是验证码登录、2是密码登录
      token: '',
      timer: 0, //验证码时间
      showTimer: true, //是否显示验证码时间
    }
  },

  methods: {
    weixinLogin(){
      uni.showLoading({
        title: '登录中'
      });
      const _this = this;
      uni.login({
        "provider": "weixin",
        "onlyAuthorize": true,
        success: function(event){
          const {code} = event
          uni.request({
            url: `/weixinLogin/${code}`,
            method: 'GET',
            header: {},
            success: (res) => {
              uni.hideLoading();
              _this.loginSuccess(res);
            }
          });
        },
        fail: function (err) {
          console.error(err)
        }
      })
    },
    // 显示隐藏密码
    changePassword: function () {
      this.showPassword = !this.showPassword;
    },
    // 判断是否显示清除按钮
    clearInput: function (event) {
      this.iphoneValue = event.detail.value;
      if (event.detail.value.length > 0) {
        this.showClearIcon = true;
      } else {
        this.showClearIcon = false;
      }
    },
    // 清除内容/隐藏按钮
    clearIcon: function () {
      this.iphoneValue = '';
      this.showClearIcon = false;
    },
    // 密码登录
    Login() {
      let that = this
      if (!that.iphoneValue) {
        uni.showToast({
          title: '请输入账号',
          icon: 'none'
        })
        return false
      }
      // 当使用密码登录并且未输入密码时
      if (!that.passwordValue) {
        uni.showToast({
          title: '请输入密码',
          icon: 'none'
        })
        return false
      }
      uni.showLoading({
        title: '登录中'
      });
      uni.request({
        url: `/appLogin`,
        method: 'POST', // 请求方法
        header: {},
        data: {
          username: that.iphoneValue,
          password: that.passwordValue
        }, //发送的数据
        success: (res) => {
          uni.hideLoading();
          that.loginSuccess(res);
        }
      })
    },

    loginSuccess(res){
      const resp = res.data;
      if (resp.code === 10000) {
        // 用户数据
        console.log('resp.data', resp.data);
        const userInfo = resp.data.userInfo;
        const menuList = resp.data.menuList;
        const token = resp.data.token.token;
        store.commit('setUserInfo', userInfo);
        store.commit('setMenuList', menuList);
        store.commit('setToken', token);
        uni.switchTab({
          url: "../user/index",
        })
        uni.showToast({
          title: '登录成功',
          icon: 'none'
        })
      } else {
        uni.showToast({
          title: '登录失败',
          icon: 'none'
        })
      }
    }
  }
}
</script>

<style scoped>
.login-content {
  padding: 70px 10px 35px;
  text-align: center;
  color: #333333;
}

.login-title {
  font-size: 26px;
  font-weight: bold;
  margin-bottom: 31px;
}

.login-content input {
  height: 50px;
  background: #F8F8F8;
  border-radius: 25px;
  text-align: left;
  padding: 15px;
  box-sizing: border-box;
  font-size: 15px;
}

.iphone,
.password,
.test {
  position: relative;
  margin-bottom: 30px;
}

.iphone .uni-icons,
.password .uni-icons {
  position: absolute;
  top: 14px;
  right: 30px;
}

.test-btn,
.password-btn {
  color: #ff8b33;
  font-size: 15px;
  text-align: right;
}

.get-test {
  color: #ff8b33;
  font-size: 15px;
  width: 122px;
  height: 50px;
  border: 1px solid #FF8B33;
  border-radius: 25px;
  line-height: 50px;
}

.test {
  display: flex;
  justify-content: space-between;
}

.login-btn {
  width: 200px;
  height: 40px;
  background: #FF8B33;
  border-radius: 36px;
  color: #fff;
  font-size: 20px;
  text-align: center;
  line-height: 40px;
  /*position: fixed;*/
  /*bottom: 60px;*/
}
.login-btn:active {
  background: #c4641b;
}
</style>