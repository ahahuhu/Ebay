<template>
	<view>
		<!-- 写一个表单 -->
		<u-form :model="user">
			<!-- item -->
			<u-form-item left-icon="man-add" label="昵称" label-width="200">
				<u-input placeholder="请输入昵称" type="text" v-model="user.nickname"></u-input>
			</u-form-item>
			<u-form-item left-icon="phone" label="注册手机" label-width="200">
				<u-input placeholder="可以更换手机号" type="text" v-model="user.account"></u-input>
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
		<u-form>
			<u-form-item left-icon="photo" label="上传头像" label-width="200">
				<view class="container">
					<button @click="chooseAvatar">选择头像</button>
					<image v-if="avatarUrl" :src="avatarUrl" mode="aspectFill" style="width: 100px; height: 100px;">
					</image>
					<!-- <view>  
					      <block v-for="(img, index) in images" :key="index">  
					        <image :src="img" mode="aspectFill" class="image-item"></image>  
					      </block>  
					</view> -->
				</view>
			</u-form-item>
		</u-form>
		<!-- 按钮 -->
		<u-button @click="submit()">确认信息并修改</u-button>

	</view>
</template>

<script>
	export default {
		data() {
			return {
				user: {
					id: "",
					account: "",
					nickname: "",
					password: ""
				},
				pass2: "",
				goinfo: {
					file: "",
					userId: ""
				},
				avatarUrl: '', // 存储用户选择的头像图片路径  
				// images:[],
			}
		},
		onShow() {
			const value = uni.getStorageSync("userinfo")
			if (value) {
				this.user.id = value.id
				this.user.account = value.account
				this.user.nickname = value.nickname
				this.user.password = value.password
			}
		},
		methods: {
			chooseAvatar() {
				uni.chooseImage({
					count: 1,
					sizeType: ['original', 'compressed'],
					sourceType: ['album', 'camera'],
					success: (res) => {
						// 获取用户选择的图片路径  
						this.avatarUrl = res.tempFilePaths[0];
						// this.images = res.tempFilePaths;
					},
					fail: (err) => {
						console.error(err);
					}
				});
			},
			// 上传头像到服务器  
			uploadAvatar() {
				if (!this.avatarUrl) {
					return;
				}
				console.log(this.avatarUrl)
				uni.uploadFile({
					url: 'http://localhost:8016/file/upload', // 替换为你的上传接口URL  
					filePath: this.avatarUrl,
					name: 'file', // 根据后端要求填写文件参数名  
					formData: {
						'userId': this.user.id // 将用户ID作为formData的一部分发送  
					},
					success: (res) => {
						console.log(res)
					},
				});

			},
			submit() {
				this.uploadAvatar()
				if (this.pass2 == this.user.password) {
					uni.request({
						url: "http://localhost:8016/doUpdate",
						data: this.user,
						method: "POST",
						success: (res) => {
							console.log(res)
							if (res.statusCode == 200) {
								uni.setStorageSync("userinfo", this.user)
								// 回退到登录
								uni.navigateBack()
							} else {
								this.$u.toast("修改失败")
							}
						}
					})
				} else {
					this.$u.toast("两次密码不一致，请检查！")
				}
			}
		}
	}
</script>

<style>
	
</style>