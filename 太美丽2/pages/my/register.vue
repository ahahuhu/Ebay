<template>
	<view>
		<!-- 写一个表单 -->
		<u-form :model="user">
			<!-- item -->
			<u-form-item left-icon="man-add" label="昵称" label-width="200">
				<u-input placeholder="请输入昵称" type="text" v-model="user.nickname"></u-input>
			</u-form-item>
			<u-form-item left-icon="phone" label="注册手机号" label-width="200">
				<u-input placeholder="手机号与账号一致" type="text" v-model="user.account"></u-input>
			</u-form-item>
			<u-form-item left-icon="lock" label="密码" label-width="200">
				<u-input placeholder="请输入密码" type="password" v-model="user.password"></u-input>
			</u-form-item>
			</u-form>
		<u-form :model="pass2">
			<u-form-item left-icon="lock" label="确定密码" label-width="200">
				<u-input placeholder="请再次输入密码" type="password" v-model="pass2"></u-input>
			</u-form-item>
		</u-form>
		<!-- 按钮 -->
		<u-button @click="signup()">确认信息并注册</u-button>
		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				user: {
					account: "",
					nickname: "",
					password: ""
				},
				pass2: ""
			}
		},
		methods: {
			signup(){
				if(this.user.password == this.pass2){
					uni.request({
						url:"http://localhost:8016/doRegister",
						data:this.user,
						method:"POST",
						success:(res)=>{
							console.log(res)
							if(res.data.code == 200){
								// 回退到登录
								uni.navigateBack()
							} else {
								this.$u.toast("注册失败")
							}
						}
					})
				}
			}
		}
	}
</script>

<style>

</style>
