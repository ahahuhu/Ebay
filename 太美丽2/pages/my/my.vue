<template>
	<view>
	<view>
		<view v-if="logined">
			<view class="u-flex user-box u-p-l-30 u-p-r-20 u-p-b-30">
				<view class="u-m-r-10">
					<image :src="uPic" shape="circle" mode="aspectFill" style="width: 85px; height: 85px;" :key="imageKey"></image>
				</view>
				<view class="u-flex-1">
					<view class="u-font-18 u-p-b-20">{{yonghu.nickname}}</view>
					<view class="u-font-14 u-tips-color">账号：{{yonghu.account}}</view>
				</view>
				<view class="u-m-l-10 u-p-10">
					<u-icon name="arrow-right" color="#969799" size="28" @click="change()"></u-icon>
				</view>
			</view>

			<view class="u-m-t-20">
				<u-cell-group>
					<u-cell-item icon="rmb-circle" title="我的发布" @click="publish"></u-cell-item>
				</u-cell-group>
			
				<u-cell-group>
					<u-cell-item icon="star" title="我的收藏" @click="store"></u-cell-item>
				</u-cell-group>
			</view>

			<view class="u-m-t-20">
				<u-cell-group>
					<u-cell-item icon="trash" title="退出登录" @click="exit()"></u-cell-item>
				</u-cell-group>
			</view>
		</view>
		<view v-else>
			<view class="u-flex user-box u-p-l-30 u-p-r-20 u-p-b-30">
				<view class="u-m-r-10">
					<u-avatar :src="pic" size="140"></u-avatar>
				</view>
				<view class="u-flex-1">
					<view class="u-font-18 u-p-b-20">用户未登录</view>
					<view class="u-font-14 u-tips-color">请登录</view>
				</view>
			</view>

			<view class="u-m-t-20">
				<u-cell-group>
					<u-cell-item icon="man-add" title="登录" @click="login"></u-cell-item>
				</u-cell-group>
			</view>

		</view>
	</view>
	<u-tabbar :list="tabbar" :mid-button="true"></u-tabbar> <!-- 导航栏 -->
	</view>
</template>

<script>
	export default {
		data() {
			return {
				pic: "/static/filmpics.avatar.png",
				show: true,
				logined: false,
				yonghu: {
					nickname: "",
					account: "",
					id: ""
				},
				uPic: "",
				// changetimes:0,
				imageKey: 0, 
				goif:{
					userId:0
				},
				tabbar:''
				
			}
		},

		onShow() { // 每次露出页面进行
			//判断用户是否登录
			this.tabbar = [{
					iconPath: "/static/images/shop(unselected).png",
					selectedIconPath: "/static/images/shop(selected).png",
					text: '首页',
					count: 0,
					isDot: false,
					customIcon: false,
					pagePath: "/pages/home/home",
				},
				{
					iconPath: "/static/images/category(unselected).png",
					selectedIconPath: "/static/images/category(selected).png",
					text: '分类',
					customIcon: false,
					pagePath: "/pages/category/category",
				},
				{
					iconPath: "/static/images/sale(unselected).png",
					selectedIconPath: "/static/images/sale(selected).png",
					text: '卖二手',
					midButton: true,
					customIcon: false,
					pagePath: "/pages/sale/sale",
				},
				{
					iconPath: "/static/images/message(unselected).png",
					selectedIconPath: "/static/images/message(selected).png",
					text: '消息',
					customIcon: false,
					pagePath: "/pages/message/message",
				},
				{
					iconPath: "/static/images/my(unselected).png",
					selectedIconPath: "/static/images/my(selected).png",
					text: '我的',
					customIcon: false,
					pagePath: "/pages/my/my",
				},
			]
			const value = uni.getStorageSync("userinfo")
			console.log(value)
			if (value) {
				// 说明有值，已登录
				console.log("已登录")
				this.logined = true
				this.yonghu.nickname = value.nickname
				this.yonghu.account = value.account
				this.yonghu.id = value.id
				this.goif.userId = value.id
				console.log(this.yonghu.id)
				// this.fetchAvatar()
				// console.log("aaa")
				// this.uPic = 'http://localhost:8016/file/imagesDownload/' + this.yonghu.id
				uni.request({
					url:'http://localhost:8016/file/avatarURLStragt',
					data:this.goif,
					success: (res) => {
						// this.changetimes+=1
						console.log("测试图片")
						console.log(res)
						// this.uPic = btoa(new Uint8Array(res.data).reduce((data, byte) => data + String.fromCharCode(byte), '')), 'base64'
						// this.uPic = 'data:image/png;base64,' + this.uPic
						this.uPic = res.data.data
						console.log(this.uPic)
						this.imageKey++
					},
					fail: (res) => {
						console.log("测试图片失败")
						console.log(res)
					}
				})
			} else {
				this.logined = false
			}
		},
		methods: {
			login() {
				uni.navigateTo({
					url: "/pages/my/login"
				})
			},
			change() {
				uni.navigateTo({
					url: "/pages/my/userinfo"
				})
			},
			exit() {
				uni.setStorageSync("userinfo", "")
				this.logined = false
			},
			store(){
				uni.navigateTo({
					url:'/pages/my/store'
				})
			},
			publish(){
				uni.navigateTo({
					url:'/pages/my/controll'
				})
			}
		}
	}
</script>

<style lang="scss">
	page {
		background-color: #ededed;
	}

	.camera {
		width: 54px;
		height: 44px;

		&:active {
			background-color: #ededed;
		}
	}

	.user-box {
		background-color: #fff;
	}
</style>