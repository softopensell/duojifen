$(function () {
    $("#jqGrid").Grid({
        url: '../goodsorderdetail/list',
        colModel: [
			{label: 'id', name: 'id', index: 'id', key: true, hidden: true},
			{label: '订单Id', name: 'orderId', index: 'order_id', width: 80},
			{label: '订单号', name: 'orderNo', index: 'order_no', width: 80},
			{label: '店铺ID', name: 'shopId', index: 'shop_id', width: 80},
			{label: '用户ID', name: 'userId', index: 'user_id', width: 80},
			{label: '商品id', name: 'goodsId', index: 'goods_id', width: 80},
			{label: '商品名称 ', name: 'goodsName', index: 'goods_name', width: 80},
			{label: '商品封面图片', name: 'goodsImgUrl', index: 'goods_img_url', width: 80},
			{label: '商品列表图片', name: 'goodsListImgUrl', index: 'goods_list_img_url', width: 80},
			{label: '规格', name: 'specification', index: 'specification', width: 80},
			{label: '数量', name: 'buyNumber', index: 'buy_number', width: 80},
			{label: '销售价格', name: 'price', index: 'price', width: 80},
			{label: '小计总价格 ', name: 'totalPrice', index: 'total_price', width: 80}]
    });
});

let vm = new Vue({
	el: '#rrapp',
	data: {
        showList: true,
        title: null,
		goodsOrderDetail: {},
		ruleValidate: {
			name: [
				{required: true, message: '名称不能为空', trigger: 'blur'}
			]
		},
		q: {
		    name: ''
		}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function () {
			vm.showList = false;
			vm.title = "新增";
			vm.goodsOrderDetail = {};
		},
		update: function (event) {
            let id = getSelectedRow("#jqGrid");
			if (id == null) {
				return;
			}
			vm.showList = false;
            vm.title = "修改";

            vm.getInfo(id)
		},
		saveOrUpdate: function (event) {
            let url = vm.goodsOrderDetail.id == null ? "../goodsorderdetail/save" : "../goodsorderdetail/update";
            Ajax.request({
			    url: url,
                params: JSON.stringify(vm.goodsOrderDetail),
                type: "POST",
			    contentType: "application/json",
                successCallback: function (r) {
                    alert('操作成功', function (index) {
                        vm.reload();
                    });
                }
			});
		},
		del: function (event) {
            let ids = getSelectedRows("#jqGrid");
			if (ids == null){
				return;
			}

			confirm('确定要删除选中的记录？', function () {
                Ajax.request({
				    url: "../goodsorderdetail/delete",
                    params: JSON.stringify(ids),
                    type: "POST",
				    contentType: "application/json",
                    successCallback: function () {
                        alert('操作成功', function (index) {
                            vm.reload();
                        });
					}
				});
			});
		},
		getInfo: function(id){
            Ajax.request({
                url: "../goodsorderdetail/info/"+id,
                async: true,
                successCallback: function (r) {
                    vm.goodsOrderDetail = r.goodsOrderDetail;
                }
            });
		},
		reload: function (event) {
			vm.showList = true;
            let page = $("#jqGrid").jqGrid('getGridParam', 'page');
			$("#jqGrid").jqGrid('setGridParam', {
                postData: {'name': vm.q.name},
                page: page
            }).trigger("reloadGrid");
            vm.handleReset('formValidate');
		},
        reloadSearch: function() {
            vm.q = {
                name: ''
            }
            vm.reload();
        },
        handleSubmit: function (name) {
            handleSubmitValidate(this, name, function () {
                vm.saveOrUpdate()
            });
        },
        handleReset: function (name) {
            handleResetForm(this, name);
        }
	}
});