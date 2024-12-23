<template>
	<view>
		<!-- 写一个表单 -->
		<!-- 把整个表单封闭到user中 -->
		<u-form :model="user">
			<!-- item -->
			<u-form-item left-icon="account" label="账号" label-width="115">
				<u-input placeholder="请输入账号" type="text" v-model="user.account"></u-input>
			</u-form-item>
			<u-form-item left-icon="lock" label="密码" label-width="115">
				<u-input placeholder="请输入密码" type="password" v-model="user.password"></u-input>
			</u-form-item>

		</u-form>
		<!-- 按钮 -->
		<u-button @click="submit()">登录</u-button>
		<u-button @click="signup()">没有账号？点击去注册</u-button>


	</view>
</template>

<script>
	export default {
		data() {
			return {
				user: {
					account: "",
					password: ""
				}
			}
		},
		methods: {
			submit() {
				uni.request({
					url: "http://localhost:8016/doLogin",
					data: this.user,
					method: "POST",
					success: (response) => {
						//输出结果
						//console.log(response.data.code)
						//判断是否登录成功
						if (response.data.code == 200) {
							//登录成功
							//1.存信息
							console.log(response.data.result)
							uni.setStorageSync("userinfo", response.data.result)
							//2.回退
							uni.showToast({
								title: '登录成功',
								icon: 'success',
								duration: 1000 // 这里设置duration为1000ms，但实际上toast的显示时间不会影响到setTimeout的延迟  
							});
							// 使用setTimeout延迟1秒后执行返回操作  
							setTimeout(() => {
								// 返回上一个页面，uni-app中通常使用uni.navigateBack()  
								uni.navigateBack();
							}, 1000); // 延迟时间为1000毫秒，即1秒  
						} else {
							//登录失败
							//提示
							this.$u.toast("登录失败")
						}
					}
				})
			},
			signup() {
				uni.navigateTo({
					url: "/pages/my/register"
				})
			}
		}
	}
</script>

<style>

</style>