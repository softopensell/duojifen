$(function () {
	/**
	 * 订单列表
	 */
    $("#jqGrid").Grid({
        url: '../goodsorder/list',
        colModel: [
			{label: 'id', name: 'id', index: 'id', key: true, hidden: true},
			{label: '订单号', name: 'orderNo', index: 'order_no', width: 150, align: 'center'},
			{label: '下单时间', name: 'createTime', index: 'create_time', align: 'center', width: 120, formatter: function (value) {
                return transDate(value);
            }},
			{label: '客户名称', name: 'userName', index: 'user_id', align: 'center', width: 120},
			{label: '联系方式', name: 'userMobile', index: 'mobile', align: 'center', width: 120},
			{label: '商品总额', name: 'totalPrice', index: 'total_price', align: 'center', width: 80, hidden: true},
			{label: '使用积分', name: 'totalIntegralPrice', index: 'total_integral_price', align: 'center', width: 80, hidden: true},
			{label: '实付金额', name: 'totalPayPrice', index: 'total_pay_price', align: 'center', width: 80},
			{label: '付款方式', name: 'payType', index: 'pay_type', width: 70, align: 'center', formatter: function (value, col, row) {
				if(row.orderStatus==1||row.orderStatus==5||row.orderStatus==6||row.orderStatus==7){
					return "--";
				}else{
					return getMacroNameByCode('payType',value);
				}
            }},
			{label: '订单状态', name: 'orderStatus', index: 'order_status', align: 'center', width: 70, formatter: function (value) {
				if(value==1){
					return ' <button class="btn btn-default btn-sm">待付款</button>';
				}else if(value==2){
					return ' <button class="btn btn-danger btn-sm">待发货</button>';
				} else if(value==3){
					return ' <button class="btn btn-warning btn-sm">已发货</button>';
				} else if(value==4){
					return ' <button class="btn btn-outline btn-sm">已完成</button>';
				} 
				return getMacroNameByCode('orderStatus',value);
            }},
			{label: '收款状态', name: 'payStatus', index: 'pay_status', align: 'center', width: 70, formatter: function (value) {
				 return getMacroNameByCode('payStatus',value);
            }},
            {label: '操作', width: 80, align: 'center', sortable: false, formatter: function (value, col, row) {
            	 var operationStr='';
                 operationStr=operationStr+'<button class="btn btn-outline btn-info" onclick="vm.lookDetail(' + row.id + ')"><i class="fa fa-info-circle"></i>&nbsp;详情</button>';
                 if(row.orderStatus==1||row.orderStatus==2){
                	 operationStr=operationStr+'<button class="btn btn-outline btn-danger" onclick="vm.toCancelOrder(\'' + row.id +'\')">取消退款</button><br/>';
                 }
            	 return operationStr;
            }}
			],
			height:"100%",
            rowNum:50,
    });
     vm.getOrderStatuss();
});
/**
 * 列表字典翻译中文名称工具方法
 * @param type
 * @param code
 * @returns
 */
function getMacroNameByCode(type,code){
	var macroList=[];
	if(type=="orderStatus"){//订单状态
		if(vm.orderStatuss.length==0){
			vm.getOrderStatuss();
		}
		macroList=vm.orderStatuss;
	}else if(type=="payType"){//支付方式
		if(vm.payTypes.length==0){
			vm.getPayTypes();
		}
		macroList=vm.payTypes;
	}else if(type=="payStatus"){//支付状态
		if(vm.payStatuss.length==0){
			vm.getPayStatuss();
		}
		macroList=vm.payStatuss;
	}
	for(var i=0;i<macroList.length;i++){
		if(macroList[i].value==code){
			return macroList[i].name;
		}
	}
	return code;
}

let vm = new Vue({
	el: '#rrapp',
	data: {
        showList: true,
        showDetail:false,
        title: null,
		goodsOrder: {},
		ruleValidate: {
			logisticsName: [
				{required: true, message: '快递公司不能为空', trigger: 'blur'}
			],
			logisticsNumber:[
				{required: true, message: '快递单号不能为空', trigger: 'blur'}
			]
		},
		/**
		 * q,订单列表查询条件
		 */
		q: {
			orderNo: '',
			userName:'',
			orderStatus:'',
			queryDate:[]
		},
		tableTotalTitle:{
			totalPriceTitle:'商品总额',
			totalIntegralPriceTitle:'积分抵扣',
			totalPayPriceTitle:'订单合计'
		},
		/**
		 * 详情页面商量列表表格表头定义
		 */
		orderDetailColumns:[
			{
				title: '',
                width:100,
                key: 'index',
                render:(h,params) => {
                  	 return h('span',{
   		               	domProps:{
   		                    innerHTML:(params.row.index)?(vm.tableTotalTitle[params.row.index]):(params.index+1)
   		                  }
                       })
                  }
            },
			{
                title: '商品图片',
                key: 'goodsListImgUrl',
                render:(h,params) => {
                  	 return h('span',{
   		               	domProps:{
   		                    innerHTML:(params.row.goodsListImgUrl)?transImg(params.row.goodsListImgUrl):''
   		                  }
                       })
                  }
            },
            {
            	title: '商品名称',
            	key: 'goodsName'
            },
            {
            	title: '商品厂商',
            	key: 'author'
            },
            {
                title: '商品标签规格',
                key: 'goodTags',
                render:(h,params) => {
                	  var tempStr="";
                	  if(params.row.goodTags){
                		  tempStr="规格："+params.row.goodTags+","; 
                	  }
                  	 return h('span',{
   		               	domProps:{
   		                    innerHTML:tempStr
   		                  }
                       })
                  }
            },
            {
            	title: '商品数量',
            	key: 'goodsCount',
            	render:(h,params) => {
            		var tempStr="";
            		if(params.row.goodsCount){
            			tempStr=tempStr+params.row.goodsCount;
            		}
            		if(params.row.specification){
            			tempStr=tempStr+params.row.specification;
            		}
            		return h('span',{
            			domProps:{
            				innerHTML:tempStr
            			}
            		})
            	}
            },
            {
                title: '商品单价',
                key: 'price'
            },
            {
                title: '商品小计',
                key: 'totalPrice'
            }
		],
		/**
		 * 商量列表表格数据，作为页面表格绑定展示
		 */
		goodsOrderDetailTableDatas:[
		],
		/**
		 * 商量列表表格数据，作为后台保存交互
		 */
		orderStatuss:[],
		payTypes:[],
		payStatuss:[]
	},
	methods: {
		query: function () {
			vm.reload();
		},
		saveOrUpdate: function (event) {
            let url = vm.goodsOrder.id == null ? "../goodsorder/save" : "../goodsorder/updateOrderAndDetail";
            Ajax.request({
			    url: url,
                params: JSON.stringify(vm.goodsOrder),
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
				    url: "../goodsorder/delete",
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
		reload: function (event) {
			vm.showList = true;
			vm.showDetail = false;
		    var confirmTimeStart='';
		    var confirmTimeEnd='';
		    if(vm.q.queryDate&&vm.q.queryDate.length==2&&vm.q.queryDate[0]&&vm.q.queryDate[1]){
		    	confirmTimeStart=vm.q.queryDate[0].dateFormat('yyyy-MM-dd 00:00:00');
		    	confirmTimeEnd=vm.q.queryDate[1].dateFormat('yyyy-MM-dd 23:59:59');
		    }
            let page = $("#jqGrid").jqGrid('getGridParam', 'page');
			$("#jqGrid").jqGrid('setGridParam', {
                postData: {
                	'orderNo': vm.q.orderNo,
                	'userName':vm.q.userName,
                	'orderStatus':vm.q.orderStatus,
                	'confirmTimeStart':confirmTimeStart,
                	'confirmTimeEnd':confirmTimeEnd
                	},
                page: page
            }).trigger("reloadGrid");
           // vm.handleReset('formValidate');
		},
        reloadSearch: function() {
            vm.q= {
    			orderNo: '',
    			userName:'',
    			orderStatus:'',
    			queryDate:[]
    		},
            vm.reload();
        },
        handleSubmit: function (name) {
            handleSubmitValidate(this, name, function () {
                vm.saveOrUpdate()
            });
        },
        handleReset: function (name) {
            handleResetForm(this, name);
        },
        /**
         * 查看订单详情
         */
        lookDetail: function (id){
        	vm.showList = false;
        	vm.showDetail = true;
		    vm.title="订单详情";
		    Ajax.request({
                url: "../goodsorder/detailinfo/"+id,
                async: true,
                successCallback: function (r) {
                    vm.goodsOrder = r.goodsOrder;
                    if(vm.goodsOrder){
                    	vm.goodsOrder.sendOrderTime=transDate(vm.goodsOrder.sendOrderTime);
                    	vm.goodsOrder.logisticsReceiveTime=transDate(vm.goodsOrder.logisticsReceiveTime);
                    	vm.goodsOrderDetailTableDatas=r.goodsOrder.goodsOrderDetailEntityList;
                    }
                }
            });
        },
        toCancelOrder: function (id){
        	confirm('确定要取消订单？', function () {
        		Ajax.request({
            		url: "../goodsorder/cancelOrder?id="+id,
            		async: true,
            		successCallback: function (r) {
            			alert('操作成功', function (index) {
                            vm.reload();
                        });
            		}
            	});	
			});
        },
        /**
         * 订单状态
         */
        getOrderStatuss: function () {
        	if(vm.orderStatuss.length>0){
        		return;
        	}
            Ajax.request({
                url: "../sys/macro/queryMacrosByValue?value=orderStatus",
                async: false,
                successCallback: function (r) {
                    vm.orderStatuss = r.list;
                }
            });
        },
        /**
         * 支付类型
         */
        getPayTypes: function () {
        	if(vm.payTypes.length>0){
        		return;
        	}
            Ajax.request({
                url: "../sys/macro/queryMacrosByValue?value=payType",
                async: false,
                successCallback: function (r) {
                    vm.payTypes = r.list;
                }
            });
        },
        /**
         * 支付状态
         */
        getPayStatuss: function () {
        	if(vm.payStatuss.length>0){
        		return;
        	}
            Ajax.request({
                url: "../sys/macro/queryMacrosByValue?value=payStatus",
                async: false,
                successCallback: function (r) {
                    vm.payStatuss = r.list;
                }
            });
        },
        confirmSubmit: function (name) {
            handleSubmitValidate(this, name, function () {
                vm.confirmShipment()
            });
        },
        /**
         * 确认发货
         */
        confirmShipment:function(){
        	let goodsOrder = {
        			id:vm.goodsOrder.id,
        			orderNo:vm.goodsOrder.orderNo,
        			logisticsName:vm.goodsOrder.logisticsName,
        			logisticsNumber:vm.goodsOrder.logisticsNumber
        	};
        	Ajax.request({
			    url: "../goodsorder/confirmShipment",
                params: JSON.stringify(goodsOrder),
                type: "POST",
			    contentType: "application/json",
                successCallback: function (r) {
                    alert('操作成功', function (index) {
                        vm.reload();
                    });
                }
			});
        },
        exportOrder: function () {
        	var confirmTimeStart='';
 		    var confirmTimeEnd='';
 		    if(vm.q.queryDate&&vm.q.queryDate.length==2&&vm.q.queryDate[0]&&vm.q.queryDate[1]){
 		    	confirmTimeStart=vm.q.queryDate[0].dateFormat('yyyy-MM-dd 00:00:00');
 		    	confirmTimeEnd=vm.q.queryDate[1].dateFormat('yyyy-MM-dd 23:59:59');
 		    	vm.q.confirmTimeStart=confirmTimeStart;
 		 		vm.q.confirmTimeEnd=confirmTimeEnd;
 		    }
        	
            exportFile('#rrapp', '../goodsorder/export',vm.q);
        },
        handleSuccessExcel: function (res, file) {
       	 vm.reload();
       },
       handleFormatError: function (file) {
           this.$Notice.warning({
               title: '文件格式不正确',
               desc: '文件 ' + file.name + ' 格式不正确。'
           });
       },
       handleMaxSize: function (file) {
           this.$Notice.warning({
               title: '超出文件大小限制',
               desc: '文件 ' + file.name + ' 太大，不能超过 2M。'
           });
       },
	}
});
