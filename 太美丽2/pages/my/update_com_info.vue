<template>
	<view class="container">

		<view class="input-group">
			<text class="label-text">商品名称</text><br>
			</br><input class="input-field" placeholder="请输入商品名称" placeholder-style="color: #999; font-size: 16px;"
				v-model="goods.name" type="text" />
		</view>

		<view class="input-group">
			<text class="label-text">商品分类</text><br>
			</br>
			<picker mode="selector" :range="categories" @change="onCategoryChange">
				<view class="picker">
					{{ goods.category !== null ? categories[goods.category] : '请选择商品分类' }}
				</view>
			</picker>
		</view>

		<view class="input-group">
			<text class="label-text">商品描述</text><br>
			</br><textarea class="textarea-field" placeholder="描述一下商品的品牌、来源..." v-model="goods.description"
				@input="handleInput" />
		</view>
		<view class="input-group">
			<text class="label-text">图片上传</text>
			<u-upload ref="uUpload1" :action="action" :auto-upload="false" :max-size="5 * 1024 * 1024" max-count="6"
				:form-data="formData" :file-list.sync="fileList" @change="handleChange" @success="handleSuccess"
				@fail="handleFail">
			</u-upload>
		</view>

		<view class="input-group">
			<text class="label-text">价格（单位：元）</text><br>
			</br><input class="input-field" placeholder="¥0.00" v-model="goods.price" type="number" />
		</view>

		<view class="input-group shipping-group">
			<text class="label-text">商品所在位置</text><br>
			</br><input class="location-label" placeholder="请输入商品所在校区和楼栋"
				placeholder-style="color: #999; font-size: 16px;" v-model="goods.deliveryArea" />
		</view>

		<button class="submit-button" @click="handleSubmit">提交修改</button>
	</view>
</template>

<script>
	export default {
		data() {
			return {

				categories: ['书籍', '电子数码', '运动户外', '生活用品', '食品', '其他'],
				goods: {
					userId: 12, //该商品所属用户的ID
					name: "",
					category: null,
					description: '',
					price: '',
					deliveryArea: '',
				},
				action: 'http://localhost:8016/file/uploadProductImage',
				formData: {
					'userId': 12,
					'productId': 37,
					'file': ""
				},
				filesArr: [],
				imageList: [], // 存储选择的图片路径列表
				fileList: [], // 存储选择的文件列表
				productId: 37,


			}
		},
		methods: {
			onCategoryChange(e) {
				this.goods.category = e.detail.value;
				console.log(this.goods.category);
			},
			handleInput(event) {
				this.goods.description = event.target.value;
			},
			handleSubmit() {
				//console.log(!this.goods.name,!this.categories[this.goods.category],!this.goods.description,!this.goods.price);
				if (!this.goods.name || !this.categories[this.goods.category] || !this.goods.description || !this.goods
					.price) {
					this.$u.toast("请填写完整信息");
					return; // 停止提交
				}

				console.log('发布内容:', {
					userId: this.goods.userId,
					name: this.goods.name,
					category: this.goods.category,
					description: this.goods.description,
					price: this.goods.price,
					deliveryArea: this.goods.deliveryArea
				});
				uni.request({
					url: "http://localhost:8016/updateProduct",
					data: this.goods,
					method: "POST",
					success: (res) => {
						console.log(res.data.code);
						console.log(res);
						if (res.data.code == 200) {
							//更新fromData的商品ID
							this.formData.productId = res.data.result.id;
							console.log(this.formData.productId);
							
							let files = this.$refs.uUpload1.lists;
							console.log(files);
							
							// 提交修改的商品图片之前，先把数据库存在的图片删除
							console.log(this.productId);
							uni.request({
								url:'http://localhost:8016/file/deleteProductImage',
								data:{
									productId:this.productId
								},
								method:"GET",
								success: (res) => {
									console.log(res)
									if(res.data.code==200){
										console.log("删除现有商品所有图片成功");
									}
									else{
										console.log("删除现有商品所有图片失败");
									}
								},
								fail: (res) => {
									console.log("请求删除现有商品所有图片失败");
								}
							})
							
							
							this.$nextTick(() => {
								if (this.$refs.uUpload1) {
									let files = this.$refs.uUpload1.lists;
									console.log(files);
									
									this.fileList = files.map(file1 => ({
									    url: file1.url,
									    // name: image.url.split('/').pop(), // 从url中提取文件名作为name
									    // status: 'success'
									}));
									//this.handleChange(this.fileList);
									console.log(this.fileList);
									//更新formData的值
									this.formData.userId = this.goods.userId;
									this.formData.file = files;

									this.$refs.uUpload1.upload();
								} else {
									console.error('uUpload1 ref is undefined');
								}
							});
							console.log("修改成功")
							uni.navigateBack()
						} else {
							console.log("修改失败，请重试")
						}
					}
				})
			},
			handleChange(fileList) {
				console.log('文件变化：', fileList);
				this.fileList = fileList;
				let files = this.$refs.uUpload1.lists;
				console.log(files);
			},
			handleSuccess(response, file) {
				console.log('上传成功：', response);
				uni.showToast({
					title: '上传成功',
					icon: 'success'
				});
			},
			handleFail(error, file) {
				console.error('上传失败：', error);
				uni.showToast({
					title: '上传失败',
					icon: 'none'
				});
			},
			radioChange(e) {
				console.log(e);
			},
			radioGroupChange(e) {
				console.log(e);
			}
		},
		//接受上个页面传递的productId参数
		onLoad: function(option) { //option为object类型，会序列化上个页面传递的参数
			console.log(option); //打印出上个页面传递的参数
			//this.productId=option.productId;
			this.productId = option.id;
			console.log(this.goods);
			//根据商品ID获取商品信息并显示
			uni.request({
				url: 'http://localhost:8016/getProduct',
				data: {
					id: this.productId,
				},
				method: "GET",
				success: (res) => {
					console.log(res);
					if (res.data.code == 200) {
						this.goods = res.data.result;
						console.log("获取商品信息成功");
					} else {
						console.log("获取商品信息失败");
					}
				},
				fail: (res) => {
					console.log(res);
					console.log("获取商品信息失败");
				}
			})
			//根据商品ID获取它的图片
			// uni.request({
			// 	url: 'http://localhost:8016/file/imagesURLStragt',
			// 	data: {
			// 		productId: this.productId
			// 	},
			// 	method: "GET",
			// 	success: (res) => {
			// 		console.log(res);
			// 		if (res.data.code == 200) {
			// 			this.fileList = res.data.result.map(image => ({
			// 			    url: image.url,
			// 			    // name: image.url.split('/').pop(), // 从url中提取文件名作为name
			// 			    // status: 'success'
			// 			}));
			// 			console.log(this.fileList);
			// 			console.log("获取商品图片成功");
			// 		} else {
			// 			console.log("获取商品图片失败");
			// 		}
			// 	}
			// })

		},
	}
</script>

<style>
	.container {
		background-image: linear-gradient(120deg, #f6d365 0%, #fda085 100%);
		padding: 20px;
		background-color: #f9f9f9;
		min-height: 100vh;
		display: flex;
		flex-direction: column;
		align-items: center;
	}

	.header {
		width: 100%;
		padding: 20px 0;
		text-align: center;
		background-color: #1065a3;
		color: white;
		font-size: 24px;
		font-weight: bold;
		border-radius: 8px;
		margin-bottom: 20px;
	}

	.input-group {
		width: 100%;
		background-color: #fff;
		padding: 20px;
		border-radius: 10px;
		margin-bottom: 20px;
		box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
	}

	.label-text {
		font-size: 18px;
		margin-bottom: 10px;
		color: #333;
	}

	.input-field,
	.textarea-field,
	.picker,
	.location-label {
		width: 95%;
		padding: 15px;
		border: 1px solid #ddd;
		border-radius: 5px;
		font-size: 16px;
	}

	.textarea-field {
		min-height: 100px;
		resize: none;
	}

	.picker {
		background-color: #fff;
		color: #1065a3;
	}

	.radio-container {
		display: flex;
		flex-direction: column;
		gap: 10px;
	}

	.radio-item {
		margin-top: 10px;
		font-size: 16px;
		display: flex;
		align-items: center;
		gap: 5px;
	}

	.submit-button {
		width: 100%;
		padding: 15px;
		background-color: #555500;
		color: #fff;
		text-align: center;
		border-radius: 10px;
		font-size: 18px;
		font-weight: bold;
		margin-top: 20px;
		cursor: pointer;
	}

	.u-upload {
		margin-top: 10px;
	}
</style>