<template>
	<view>
	<view class="category-wrap">
		<view class="order_product">
			<!--类别列表-->
			<view class="category-content">
				<view class="cotegory-type cotegory-type-3">
					<view class="category-tab">
						<scroll-view scroll-y="true" class="scroll-Y" :scroll-into-view="tocate"
							scroll-with-animation="true">
							<view :class="index == select_index ? 'item active' : 'item'"
								v-for="(item, index) in listData" :key="index"
								@click="selectCategory(item.category_id, index)" :id="'pro_' + item.category_id">
								<text style="white-space: pre;">{{ item.name.replace(' ','\n')}}</text>
							</view>
						</scroll-view>
					</view>
					<view class="category-content">
						<scroll-view scroll-y="true" class="scroll-Y scroll-3" :scroll-into-view="toview"
							scroll-with-animation="true" @scroll="cateScroll">
							<block v-for="(item, index) in listData" :key="index">
								<view  class="item aaaaa" :id="'pro_' + item.category_id">
									<view v-for="(item1, i) in item.productList" :key="i" class="list_pro"
										:id="item1.id" @click="getdetail(item1.id, item1)">
										<image class="pro_img_info" :src="item1.url" mode="aspectFill">
										</image>
										<view class="detail">
											<text class="type-name">{{ item1.name}}</text>
											<text class="selling_point">{{item1.description}}
											</text>
											<text class="price">￥{{ item1.price }}</text>

										</view>
									</view>
								</view>
							</block>
						</scroll-view>
					</view>
				</view>
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
				tabbar:'',
				toview: "",
				tocate:'',
				select_index: 0,
				listData: [{
						name: '教材书籍',
						category_id: '1',
						productList: []
					},
					{
						name: '电子数码',
						category_id: '2',
						productList: []
					},
					{
						name: '运动户外',
						category_id: '3',
						productList: []
					},
					{
						name: '生活用品',
						category_id: '4',
						productList: []
					},
					{
						name: '休闲美食',
						category_id: '5',
						productList: []
					},
					{
						name: '其他',
						category_id: '6',
						productList: []
					}
				],
			}
		},
		onLoad() {
			this.getproducts()
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
			getproducts(){
				for(let i=0;i<6;i++){
					uni.request({
						url:'http://localhost:8016/searchProductByCategory?cate='+i,
						method:'GET',
						success: (res) => {
							if(res.data.code==200){
								console.log("根据类别获取商品成功")
								this.listData[i].productList=res.data.result
							}else{
								console.log("根据类别获取商品失败")
							}
						},fail: (res) => {
							console.log("请求失败")
						}
					})
				}
			},
			getdetail(item){
				console.log("点击了:"+item)
				uni.navigateTo({
					url:`/pages/home/commodity?id=${item}`
				})
			},
			/*选择分类*/
			selectCategory(e, index) {
				this.toview = "pro_" + e;
				this.select_index = index;
			},
			cateScroll() {
				const query = uni.createSelectorQuery().in(this);
				query.selectAll(".aaaaa")
					.boundingClientRect((data) => {
						for (var cate = 0; cate < data.length; cate++) {
							if (data[cate].top < 200 && data[cate].top > 0) {
								this.toview = data[cate].id;
								this.select_index = cate;
							}
						}
					})
					.exec();
			},
		}
	}
</script>

<style lang="less">
	/**/
.category-wrap {  
    background-color: #fff;  
}  
  
.category-content {  
    margin-top: 0px;  
    overflow: hidden;  
}  
  
.cotegory-type {  
    width: 100%;  
}  
  
.category-tab {  
    width: 87px;  
    background: #f5f5f5;  
    overflow-y: scroll;  
    padding-bottom: 22px;  
  
    .item {  
        position: relative;  
        padding: 20px;  
        font-size: 16px;  
        text-align: center;  
        color: #6f6f6f;  
        display: inline-block;  
        width: 100%;  
        box-sizing: border-box;  
  
        &.active {  
            background: #c9ebc9;  
            color: #000;  
            font-weight: bold;  
        }  
    }  
}  
  
.cotegory-type-3 .category-content {  
    flex: 1;  
    background-color: #ffe9dd;  
}  
  
.scroll-3 {  
    height: 85vh;  
    margin: 0 10px;  
    padding-bottom: 20px;  
}  
  
.cotegory-type-3 {  
    display: flex;  
  
    .scroll-3 .item {  
        .detail {  
            vertical-align: top;  
            width: 51%;  
            position: relative;  
            padding-top: 10px;  
        }  
  
        .list_pro {  
            width: 94%;  
            background: #fff;  
            border-radius: 10px;  
            margin: 10px 0;  
            padding: 10px;  
            box-sizing: border-box;  
            display: flex;  
            justify-content: space-between;  
  
            .pro_img_info {  
                width: 45%;  
                height: 220rpx;  
                border-radius: 10px;  
            }  
  
            .type-name {  
                font-size: 16px;  
                display: block;  
                -webkit-box-orient: vertical;  
                -webkit-line-clamp: 2;  
                overflow: hidden;  
                color: #343434;  
                font-weight: bold;  
            }  
  
            .selling_point {  
                display: block;  
                color: #999;  
                height: 95rpx;  
                overflow: hidden;  
                padding: 4px 0;  
                font-size: 12px;  
                line-height: 20px;  
            }  
  
            .price {  
                font-size: 19px;  
                color: #ff0000;  
                font-weight: 600;  
            }  
        }  
    }  
}
</style>