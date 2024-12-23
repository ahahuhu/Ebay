<template>
	<view class="chat">
		<!-- 顶部标题 -->
		<view class="topTabbar">
			<!-- 返回图标 -->
			<u-icon class="icon" name="arrow-left" size="20px" color="#000" @click="goback()"></u-icon>
			<!-- 标题 -->
			<view class="text">{{your.name}}</view>
		</view>
		<scroll-view :style="{height: `${windowHeight-inputHeight - 180}rpx`}" id="scrollview" scroll-y="true"
			:scroll-top="scrollTop" class="scroll-view">
			<!-- 聊天主体 -->
			<view id="msglistview" class="chat-body" v-for="(item,index) in msgList" :key="index">
				<!-- 聊天记录 -->
				<!-- 自己发的消息 -->
				<view v-if="item.type=='my'">
					<view class="item self">
						<!-- 文字内容 -->
						<view class="content right">
							{{item.msg}}
						</view>
						<!-- 头像  换成我的头像-->
						<image class="avatar" :src="myavatar">
						</image>
					</view>
				</view>
				<view v-else><!-- 对方发的消息 -->
					<view class="item Ai">
						<!-- 头像 -->
						<image class="avatar" :src="your.avatar">
						</image>
						<!-- 文字内容 -->
						<view class="content left">
							{{item.msg}}
						</view>
					</view>
				</view>
			</view>
		</scroll-view>
		<!-- 底部消息发送栏 -->
		<!-- 用来占位，防止聊天消息被发送框遮挡 -->
		<view class="chat-bottom" :style="{height: `${inputHeight}rpx`}">
			<view class="send-msg" :style="{bottom:`${keyboardHeight - 60}rpx`}">
				<view class="uni-textarea">
					<textarea v-model="chatMsg" maxlength="300" confirm-type="send" @confirm="handleSend"
						placeholder="快来聊天吧~" :show-confirm-bar="false" :adjust-position="false" @linechange="sendHeight"
						@focus="focus" @blur="blur" auto-height></textarea>
				</view>
				<button @click="handleSend" class="send-btn">发送</button>
			</view>
		</view>
	</view>
</template>
<script>
	export default {
		data() {
			return {
				//键盘高度
				keyboardHeight: 0,
				//底部消息发送高度
				bottomHeight: 0,
				//滚动距离
				scrollTop: 0,
				myId: 12, //我的ID
				myavatar: '', //我的头像
				//发送的消息
				chatMsg: "",
				yourmsgList: [], //接受对方的消息
				mymsgList: [], //存储我发出的消息
				tempmsgList: [], //判断和上一次接受的消息是否相同
				msgList: [], //按顺序显示双方的消息
				pollInterval: '', //轮询时间间隔的ID
				timer: '',
				your: { //对方的信息
					id: 1,
					name: '张三',
					avatar: ''
				}
			}
		},
		updated() {
			//页面更新时调用聊天消息定位到最底部
			this.scrollToBottom();
		},
		computed: {
			windowHeight() {
				return this.rpxTopx(uni.getSystemInfoSync().windowHeight)
			},
			// 键盘弹起来的高度+发送框高度
			inputHeight() {
				return this.bottomHeight + this.keyboardHeight
			}
		},
		onLoad(user) {
			const value=uni.getStorageSync("userinfo")
			console.log("我的信息:"+value.id+value.avatarName+value.nickname)
				this.myId=value.id
				this.myavatar=value.avatarName
			console.log("界面加载")
			this.your = user
			console.log("与我聊天的用户：" + this.your.id + this.your.name)
			// 读取存储的消息  
			const storedMessages = uni.getStorageSync(`chatMessages_${this.myId}_${this.your.id}`);
			if (storedMessages) {
				this.msgList = JSON.parse(storedMessages); //读取后将历史消息复制给msgList
			};
			uni.onKeyboardHeightChange(res => {
				this.keyboardHeight = this.rpxTopx(res.height)
				if (this.keyboardHeight < 0) this.keyboardHeight = 0;
			});
			this.startInterval()
		},
		onUnload() { //离聊天界面时触发  
			// 清除轮询间隔  
			this.clearTimer()
			// 在页面卸载时，将msgList保存到本地缓存  
			// 假设每个用户的消息都保存在不同的缓存项中，缓存项的键名包含用户ID  
			const userId = this.myId; // 获取当前用户的ID  
			const cachedMessages = JSON.stringify(this.msgList); // 将msgList转换为JSON字符串  
			uni.setStorageSync(`chatMessages_${this.myId}_${this.your.id}`, cachedMessages); // 保存到本地缓存  

		},
		methods: {
			startInterval() {
				console.log("开始计时器")
				this.pollInterval = setInterval(() => {
					this.fetchNewMessages(); //返回一个唯一的间隔ID
				}, 1000);
			},
			clearTimer() {
				console.log("消除计时器")
				if (this.pollInterval) {
					clearInterval(this.pollInterval);
					this.pollInterval = null;
					console.log('轮询已清除');
				}
			},
			goback() {
	           uni.navigateBack()
			},
			focus() {
				this.scrollToBottom()
			},
			blur() {
				this.scrollToBottom()
			},
			// px转换成rpx
			rpxTopx(px) {
				let deviceWidth = uni.getSystemInfoSync().windowWidth
				let rpx = (750 / deviceWidth) * Number(px)
				return Math.floor(rpx)
			},
			// 监视聊天发送栏高度
			sendHeight() {
				setTimeout(() => {
					let query = uni.createSelectorQuery();
					query.select('.send-msg').boundingClientRect()
					query.exec(res => {
						this.bottomHeight = this.rpxTopx(res[0].height)
					})
				}, 10)
			},
			// 滚动至聊天底部
			scrollToBottom() {
				console.log("滚动到底部")
				this.$nextTick(() => { // 使用 Vue 的 nextTick 来确保 DOM 已更新  
					setTimeout(() => {
						let query = uni.createSelectorQuery().in(this);
						query.select('#scrollview').boundingClientRect();
						query.select('#msglistview').boundingClientRect();
						query.exec((res) => {
							if (res[0] && res[1] && res[1].height > res[0].height) {
								this.scrollTop = this.rpxTopx(res[1].height - res[0].height);
							}
						});
					}, 15);
				});
			},
			// 发送消息
			handleSend() {
				//如果消息不为空
				if (!this.chatMsg || !/^\s+$/.test(this.chatMsg)) {
					let obj = {
						type: 'my',
						msg: this.chatMsg
					}
					this.msgList.push(obj);
					// 存储更新后的消息列表  
					uni.request({
						url: 'http://localhost:8016/order/message', // 假设的后端接收消息的API端点  
						method: 'POST', // 发送POST请求  
						data: {
							content: this.chatMsg, // 发送的消息内容 
							receiverId: this.your.id,
							senderId: this.myId
						},
						success: (res) => {
							// 处理响应，比如显示发送成功提示  
							console.log('Message sent successfully', res.data);
							this.scrollToBottom();
						},
						fail: (err) => {
							// 处理发送失败的情况  
							console.error('Failed to send message', err);
						}
					});
					this.chatMsg = '';
				} else {
					this.$modal.showToast('不能发送空白消息')
				}
			},
			// 轮询获取新消息的方法  
			fetchNewMessages() {
				uni.request({
					// 获取对方发的消息
					url: `http://localhost:8016/order/requiremessage/${encodeURIComponent(this.your.id)}/${encodeURIComponent(this.myId)}`,
					method: 'GET',
					success: (res) => {
						// 处理响应数据  
						console.log("获取信息 ")
						console.log(res)
						this.scrollToBottom();
						if (res.data.code == 200) {
							console.log("获取新信息成功")

							for (let i = (res.data.data.length - 1); i >= 0; i--) {
								let obj1 = {
									type: 'your',
									msg: res.data.data[i].content
								}
								this.msgList.push(obj1)
								console.log(this.msgList)
							}
							// this.lastMessageId = res.data.lastMessageId; // 假设服务器返回了最后一条消息的ID  
							// 可能还需要滚动到底部等操作  
							// this.scrollToBottom();  
						}
					},
					fail: (err) => {
						// 处理请求失败的情况  
						console.error('Failed to fetch new messages:', err);
					}
				})
			}
		}
	}
</script>
<style lang="scss" scoped>
	$chatContentbgc: #C2DCFF;
	$sendBtnbgc: #4F7DF5;

	view,
	button,
	text,
	input,
	textarea {
		margin: 0;
		padding: 0;
		box-sizing: border-box;
	}

	/* 聊天消息 */
	.chat {
		.topTabbar {
			width: 100%;
			height: 90rpx;
			line-height: 90rpx;
			display: flex;
			margin-top: 80rpx;
			justify-content: space-between;

			.icon {
				margin-left: 20rpx;
			}

			.text {
				margin: auto;
				font-size: 16px;
				font-weight: 700;
			}

			.button {
				width: 10%;
				margin: auto 20rpx auto 0rpx;
			}
		}

		.scroll-view {
			::-webkit-scrollbar {
				display: none;
				width: 0 !important;
				height: 0 !important;
				-webkit-appearance: none;
				background: transparent;
				color: transparent;
			}

			// background-color: orange;
			background-color: #F6F6F6;

			.chat-body {
				display: flex;
				flex-direction: column;
				padding-top: 23rpx;
				// background-color:skyblue;

				.self {
					justify-content: flex-end;
				}

				.item {
					display: flex;
					padding: 23rpx 30rpx;
					// background-color: greenyellow;

					.right {
						background-color: $chatContentbgc;
					}

					.left {
						background-color: #FFFFFF;
					}

					// 聊天消息的三角形
					.right::after {
						position: absolute;
						display: inline-block;
						content: '';
						width: 0;
						height: 0;
						left: 100%;
						top: 10px;
						border: 12rpx solid transparent;
						border-left: 12rpx solid $chatContentbgc;
					}

					.left::after {
						position: absolute;
						display: inline-block;
						content: '';
						width: 0;
						height: 0;
						top: 10px;
						right: 100%;
						border: 12rpx solid transparent;
						border-right: 12rpx solid #FFFFFF;
					}

					.content {
						position: relative;
						max-width: 486rpx;
						border-radius: 8rpx;
						word-wrap: break-word;
						padding: 24rpx 24rpx;
						margin: 0 24rpx;
						border-radius: 5px;
						font-size: 32rpx;
						font-family: PingFang SC;
						font-weight: 500;
						color: #333333;
						line-height: 42rpx;
					}

					.avatar {
						display: flex;
						justify-content: center;
						width: 78rpx;
						height: 78rpx;
						background: $sendBtnbgc;
						border-radius: 50rpx;
						overflow: hidden;

						image {
							align-self: center;
						}

					}
				}
			}
		}

		/* 底部聊天发送栏 */
		.chat-bottom {
			width: 100%;
			height: 100rpx;
			background: #F4F5F7;
			transition: all 0.1s ease;

			.send-msg {
				display: flex;
				align-items: flex-end;
				padding: 16rpx 30rpx;
				width: 100%;
				min-height: 177rpx;
				position: fixed;
				bottom: 0;
				background: #fff;
				transition: all 0.1s ease;
			}

			.uni-textarea {
				padding-bottom: 70rpx;

				textarea {
					width: 537rpx;
					min-height: 75rpx;
					max-height: 500rpx;
					background: #f1f1f1;
					border-radius: 40rpx;
					font-size: 32rpx;
					font-family: PingFang SC;
					color: #333333;
					line-height: 74rpx;
					padding: 5rpx 8rpx;
					text-indent: 30rpx;
				}
			}

			.send-btn {
				display: flex;
				align-items: center;
				justify-content: center;
				margin-bottom: 76rpx;
				margin-left: 25rpx;
				width: 120rpx;
				height: 75rpx;
				background: #ed5a65;
				border-radius: 50rpx;
				font-size: 28rpx;
				font-family: PingFang SC;
				font-weight: 500;
				color: #FFFFFF;
				line-height: 28rpx;
			}
		}
	}
</style>