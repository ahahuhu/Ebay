<template>
	<view>
		<view class="secondhand-button">
			<button class="transparent-button1" @click="Secondhand_Click_Button1()":plain="true" ></button>
			<button class="transparent-button2" @click="Secondhand_Click_Button2()":plain="true" ></button>
			<button class="transparent-button3" @click="Secondhand_Click_Button3()":plain="true" ></button>
		</view>
		<u-tabbar :list="tabbar" :mid-button="true"></u-tabbar>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				tabbar: ''
			}
		},
		onLoad() {
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
		},
		methods: {
			Secondhand_Click_Button1() {
				const value=uni.getStorageSync("userinfo")
				 if(!value){
					 uni.showToast({
					 	title: '未登录！',
					 	icon: 'none',
					 	duration: 1000 // 这里设置duration为1000ms，但实际上toast的显示时间不会影响到setTimeout的延迟  
					 });
					 return
				 }
				uni.navigateTo({
					url:"/pages/sale/post"
				})
				console.log("发闲置")
			},
			Secondhand_Click_Button2() {
				const value=uni.getStorageSync("userinfo")
				 if(!value){
					 uni.showToast({
					 	title: '未登录！',
					 	icon: 'none',
					 	duration: 1000 // 这里设置duration为1000ms，但实际上toast的显示时间不会影响到setTimeout的延迟  
					 });
					 return
				 }
				uni.navigateTo({
					url:"/pages/sale/want"
				})
				console.log("收闲置")
			},
			Secondhand_Click_Button3() {
				const value=uni.getStorageSync("userinfo")
				 if(!value){
					 uni.showToast({
					 	title: '未登录！',
					 	icon: 'none',
					 	duration: 1000 // 这里设置duration为1000ms，但实际上toast的显示时间不会影响到setTimeout的延迟  
					 });
					 return
				 }
				uni.navigateTo({
					url:"/pages/sale/allwant"
				})
				console.log("求购信息大厅")
			},
		}
	}
</script>

<style lang="scss" scoped>
	.secondhand-button {
		background-image: url("/static/filmpics/sale_background3.png");
		position: relative;
		/* 这是必要的，以便子元素可以使用绝对定位 */
		background-size: cover;
		/* 背景图覆盖整个元素 */
		width: 100%;
		height: 800px;
		/* 设置高度 */
		// display: flex; /* 使得内容居中（如果需要） */  
		// justify-content: center; /* 水平居中 */  
		// align-items: center; /* 垂直居中 */  
	}
	.transparent-button1 {
		position: absolute;
		/* 使用绝对定位 */
		top: 335px;
		height: 90px;
		width: 95%;
		/* 距离容器顶部100px的位置（根据需要调整） */
		left: 50%;
		/* 水平居中（如果需要） */
		transform: translateX(-50%);
		/* 修正left: 50%带来的偏移，实现真正的水平居中 */
		padding: 10px 20px;
		/* 根据需要调整内边距 */
		border: none;
		outline: none;
		/* 移除边框 */
		background-color: rgba(0, 0, 0, 0);
		/* 完全透明背景 */
		cursor: pointer;
		/* 鼠标悬停时显示指针样式 */
		// ...（其他按钮样式，如需要）  
		border-radius: 20px;
	}
	.transparent-button2 {
		position: absolute;
		/* 使用绝对定位 */
		top: 445px;
		height: 90px;
		width: 95%;
		/* 距离容器顶部100px的位置（根据需要调整） */
		left: 50%;
		/* 水平居中（如果需要） */
		transform: translateX(-50%);
		/* 修正left: 50%带来的偏移，实现真正的水平居中 */
		padding: 10px 20px;
		/* 根据需要调整内边距 */
		border: none;
		outline: none;
		/* 移除边框 */
		background-color: rgba(0, 0, 0, 0);
		/* 完全透明背景 */
		cursor: pointer;
		/* 鼠标悬停时显示指针样式 */
		// ...（其他按钮样式，如需要）  
		border-radius: 20px;
	}
	.transparent-button3 {
		position: absolute;
		/* 使用绝对定位 */
		top: 550px;
		height: 90px;
		width: 95%;
		/* 距离容器顶部100px的位置（根据需要调整） */
		left: 50%;
		/* 水平居中（如果需要） */
		transform: translateX(-50%);
		/* 修正left: 50%带来的偏移，实现真正的水平居中 */
		padding: 10px 20px;
		/* 根据需要调整内边距 */
		border: none;
		outline: none;
		/* 移除边框 */
		background-color: rgba(0, 0, 0, 0);
		/* 完全透明背景 */
		cursor: pointer;
		/* 鼠标悬停时显示指针样式 */
		// ...（其他按钮样式，如需要）  
		border-radius: 20px;
	}
</style>