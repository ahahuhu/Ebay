<template>
	<view>
		<u-search border-color="#9a9a9a" bg-color="#ffffff" placeholder="搜索需求商品" margin=8px :clearabled="true"
			:show-action="true" action-text="搜索" :action-style="SearchButtonStyle" v-model="keyword"
			@custom="onSearch()"></u-search>
		<view class="comment" v-for="(res, index) in infoList" :key="index">

			<view class="left">
				<image :src="res.pic" mode="aspectFill"></image>
			</view>
			<view class="right">
				<view class="top">
					<view class="name">{{ res.nickname }}</view>
				</view>
				<view class="top">
					<view class="title">#{{ res.name }}</view>
				</view>
				<view class="content">{{ res.description }}</view>
				<view class="money">预期：￥{{res.price}}</view>
				<view class="bottom">
					<view>{{res.address}}</view>
					<text>·</text>
					{{ res.goodTime }}
					<view class="reply"@click="communicate(res)">与Ta联系</view>
				</view>
			</view>
			
		</view>
		<u-divider>没有更多了</u-divider>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				commentList: [],
				infoList: [],
				ainfo: {
					seekerId: 0,
					userId: 0,
					name: "",
					description: "1233",
					price: 0,
					address: "",
					releaseTime: "",
					nickname: "",
					pic: "",
					isOpen: 0,
				},
				SearchButtonStyle: {
					backgroundColor: '#ff5511', // 定义控件的背景颜色  
					// fontSize: '13px', // 定义控件的字体大小 
					color: '#ffffff',
					borderRadius: '8px', // 添加圆角
					// 你可以继续添加其他需要的样式属性  
				},
				keyword: "",
			};
		},
		onLoad() {
			console.log("onShow")
			this.getComment();
		},
		methods: {
			// 评论列表
			getComment() {
				this.infoList = []
				console.log("获取需求列表")
				uni.request({
					url: "http://localhost:8016/allUsersSeeker",
					success: (res) => {
						console.log(res)
						if (res.statusCode == 200) {
							this.infoList = res.data.result
						}
						console.log(this.infoList)
					},
					fail: (res) => {
						console.log(res)
					}
				})
			},
			onSearch() {
				uni.setStorageSync("wantkey", this.keyword)
				uni.navigateTo({
					url: '/pages/sale/want_info'
				})
			},
			communicate(res){
				uni.navigateTo({
					url:`/pages/message/chattingbox?id=${res.userId}&name=${res.nickname}&avatar=${res.pic}`
				})
			}
		}
	};
</script>

<style lang="scss" scoped>
	.comment {
		display: flex;
		padding: 30rpx;

		.left {
			image {
				width: 64rpx;
				height: 64rpx;
				border-radius: 50%;
				background-color: #f2f2f2;
			}
		}

		.right {
			flex: 1;
			padding-left: 20rpx;
			font-size: 30rpx;

			.top {
				display: flex;
				justify-content: space-between;
				align-items: center;
				margin-bottom: 10rpx;

				.name {
					color: #c8c2b9;
				}

				.title {
					color: #f07b3f;
					font-size: 40rpx;
				}
			}

			.bottom {
				margin-top: 20rpx;
				display: flex;
				font-size: 24rpx;
				color: #9a9a9a;

				.reply {
					color: #FFCC22;
					margin-left: 10rpx;
				}
			}

			.content {}

			.money {
				color: red;
			}
		}
	}
</style>