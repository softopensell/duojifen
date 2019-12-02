$(function () {
    $("#jqGrid").Grid({
        url: '../shop/list',
        colModel: [
			{label: 'id', name: 'id', index: 'id', key: true, hidden: true},
			{label: '商家类别 ', name: 'shopType', index: 'shop_type', width: 80,formatter: function (value) {
                return getMacroNameByCode('shopType',value);
            }},
			{label: '商店名称', name: 'shopName', index: 'shop_name', width: 80},
			{label: '商店LOGO', name: 'shopLogo', index: 'shop_logo', width: 80,formatter: function (value) {
                return transImg(value);
            }},
			{label: '商店描述', name: 'shopDesc', index: 'shop_desc', width: 80, hidden: true},
			{label: '联系人', name: 'shopContactName', index: 'shop_contact_name', width: 80},
			{label: '联系方式', name: 'shopContactPhone', index: 'shop_contact_phone', width: 80},
			{label: '省', name: 'provinceId', index: 'province_id', width: 80, hidden: true},
			{label: '市', name: 'cityId', index: 'city_id', width: 80, hidden: true},
			{label: '区', name: 'regionId', index: 'region_id', width: 80, hidden: true},
			{label: '地址', name: 'address', index: 'address', width: 80, hidden: true},
			{label: '注意备注', name: 'remark', index: 'remark', width: 80, hidden: true},
			{label: '状态', name: 'statu', index: 'statu', width: 60,formatter: function (value) {
                return getMacroNameByCode('shopStatus',value);
            }},
			{label: '创建用户', name: 'createUserId', index: 'create_user_id', width: 80, hidden: true},
			{label: '创建用户', name: 'createUserName', index: 'createUserName', width: 80},
			{label: '商品数量', name: 'goodsSum', index: 'goods_sum', width: 80},
			{label: '订单数量', name: 'orderSum', index: 'order_sum', width: 80},
			{label: '公司编码', name: 'companySn', index: 'company_sn', width: 80},
			{label: '创建时间', name: 'createTime', index: 'create_time', width: 80, formatter: function (value) {
                return transDate(value);
            }},
			{label: '修改时间', name: 'updateTime', index: 'update_time', width: 80, hidden: true},
			{label: '审核状态', name: 'auditStatu', index: 'audit_statu', width: 100, formatter: function (value, col, row) {
				if(value == '0'){
					var btnstr='<button class="btn btn-outline btn-primary" onclick="vm.toAudit(\'' + row.id +'\',\''+ row.shopName +'\',\'1\')"><i class="fa fa-info-circle"></i>&nbsp;通过</button>';
					btnstr+='&nbsp;<button class="btn btn-outline btn-warning" onclick="vm.toAudit(\'' + row.id +'\',\''+ row.shopName +'\',\'2\')"><i class="fa fa-info-circle"></i>&nbsp;拒绝</button>';
					return btnstr;
				}else if(value=='1'){
					return '<span class="label label-success">'+getMacroNameByCode('shopAuditStatus',value)+'</span>';
				}else{
					return '<span class="label label-danger">'+getMacroNameByCode('shopAuditStatus',value)+'</span>';
				}
            }},
            {label: '操作', width: 100, align: 'center', sortable: false, formatter: function (value, col, row) {
                return '<button class="btn btn-outline btn-info" onclick="vm.lookDetail(' + row.id + ')"><i class="fa fa-info-circle"></i>&nbsp;详情</button>';
            }}]
    });
    /**
     * 客户列表，弹出框显示，新增代下订单选择客户
     */
     $("#userjqGrid").Grid({
    	 url: '../user/list',
    	 datatype:'local',
         colModel: [
 			{label: 'id', name: 'userId', index: 'user_id', key: true, hidden: true},
 			{label: '电话', name: 'mobile', index: 'mobile', width: 240},
 			{label: '账号', name: 'userName', index: 'user_name', width: 240}
 			]
	});
});
/**
 * 列表字典翻译中文名称工具方法
 * @param type
 * @param code
 * @returns
 */
function getMacroNameByCode(type,code){
	var macroList=[];
	if(type=="shopType"){
		if(vm.shopTypes.length==0){
			vm.getShopTypes();
		}
		macroList=vm.shopTypes;
	}
	if(type=="shopStatus"){
		if(vm.shopStatuss.length==0){
			vm.getShopStatuss();
		}
		macroList=vm.shopStatuss;
	}
	if(type=="shopAuditStatus"){
		if(vm.shopAuditStatuss.length==0){
			vm.getShopAuditStatuss();
		}
		macroList=vm.shopAuditStatuss;
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
		shop: {
			shopLogo:'',
			idcardUpImg:'',
			idcardDownImg:'',
			handIdcardImg:'',
			businessLicenseImg:'',
			auditStatu:0,
			goodsSum:0,
			orderSum:0,
			auditOpinion:'',
			createUserId:1,
			createUserName:''
		},
		ruleValidate: {
			shopName: [
				{required: true, message: '店铺不能为空', trigger: 'blur'}
			]
		},
		q: {
			shopName: ''
		},
		/**
		 * 用户列表查询条件
		 */
		uq:{
			userName:''
		},
		shopTypes:[],
		shopStatuss:[],
		shopAuditStatuss:[],
		provinces:[],
		citys:[],
		areas:[]
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function () {
			vm.showList = false;
			vm.title = "新增";
			vm.shop = {
					shopType:1,
					shopLogo:'',
					idcardUpImg:'',
					idcardDownImg:'',
					handIdcardImg:'',
					businessLicenseImg:'',
					statu:0,
					auditStatu:1,
					goodsSum:0,
					orderSum:0,
					createUserId:1,
					createUserName:'',
					auditOpinion:''
			};
			vm.provinces=[];
			vm.citys=[];
			vm.areas=[];
			vm.getProvinces(0);
			vm.getShopTypes();
			vm.getShopStatuss();
			vm.getShopAuditStatuss();
		},
		update: function (event) {
            let id = getSelectedRow("#jqGrid");
			if (id == null) {
				return;
			}
			vm.showList = false;
            vm.title = "修改";
            vm.provinces=[];
			vm.citys=[];
			vm.areas=[];
            vm.getInfo(id)
            vm.getShopTypes();
			vm.getShopStatuss();
			vm.getShopAuditStatuss();
		},
		saveOrUpdate: function (event) {
            let url = vm.shop.id == null ? "../shop/save" : "../shop/update";
            Ajax.request({
			    url: url,
                params: JSON.stringify(vm.shop),
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
				    url: "../shop/delete",
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
                url: "../shop/info/"+id,
                async: false,
                successCallback: function (r) {
                    vm.shop = r.shop;
                	if(vm.shop.shopType){
                		vm.shop.shopType=vm.shop.shopType+"";
                	}
                	if(vm.shop.statu){
                		vm.shop.statu=vm.shop.statu+"";
                	}
                	vm.getProvinces(0);
                	if(vm.shop.provinceId){
                		vm.getCitys(vm.shop.provinceId);
                	}
                	if(vm.shop.cityId){
                		vm.getAreas(vm.shop.cityId);
                	}
                }
            });
		},
		reload: function (event) {
			vm.showList = true;
			vm.showDetail=false;
            let page = $("#jqGrid").jqGrid('getGridParam', 'page');
			$("#jqGrid").jqGrid('setGridParam', {
                postData: {'shopName': vm.q.shopName},
                page: page
            }).trigger("reloadGrid");
            vm.handleReset('formValidate');
		},
        reloadSearch: function() {
            vm.q = {
            		shopName: ''
            }
            vm.shop={
    			shopLogo:'',
    			auditStatu:1,
    			goodsSum:0,
    			orderSum:0,
    			auditOpinion:''
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
        },
        /**
         * 商家类型
         */
        getShopTypes: function () {
        	if(vm.shopTypes.length>0){
        		return;
        	}
            Ajax.request({
                url: "../sys/macro/queryMacrosByValue?value=shopType",
                async: false,
                successCallback: function (r) {
                    vm.shopTypes = r.list;
                }
            });
        },
        /**
         * 店铺状态
         */
        getShopStatuss: function () {
        	if(vm.shopStatuss.length>0){
        		return;
        	}
            Ajax.request({
                url: "../sys/macro/queryMacrosByValue?value=shopStatus",
                async: false,
                successCallback: function (r) {
                    vm.shopStatuss = r.list;
                }
            });
        },
        /**
         * 店铺审核
         */
        getShopAuditStatuss: function () {
        	if(vm.shopAuditStatuss.length>0){
        		return;
        	}
            Ajax.request({
                url: "../sys/macro/queryMacrosByValue?value=shopAuditStatus",
                async: false,
                successCallback: function (r) {
                    vm.shopAuditStatuss = r.list;
                }
            });
        },
        handleFormatError: function (file) {
            this.$Notice.warning({
                title: '文件格式不正确',
                desc: '文件 ' + file.name + ' 格式不正确，请上传 jpg 或 png 格式的图片。'
            });
        },
        handleMaxSize: function (file) {
            this.$Notice.warning({
                title: '超出文件大小限制',
                desc: '文件 ' + file.name + ' 太大，不能超过 2M。'
            });
        },
        handleSuccessShopLogo: function (res, file) {
            vm.shop.shopLogo = file.response.url;
        },
        eyeImageShopLogo: function () {
            var url = vm.shop.shopLogo;
            eyeImage(url);
        },
        handleSuccessIdcardUpImg: function (res, file) {
            vm.shop.idcardUpImg = file.response.url;
        },
        eyeImageIdcardUpImg: function () {
            var url = vm.shop.idcardUpImg;
            eyeImage(url);
        },
        handleSuccessIdcardDownImg: function (res, file) {
            vm.shop.idcardDownImg = file.response.url;
        },
        eyeImageIdcardDownImg: function () {
            var url = vm.shop.idcardDownImg;
            eyeImage(url);
        },
        handleSuccessHandIdcardImg: function (res, file) {
            vm.shop.handIdcardImg = file.response.url;
        },
        eyeImageHandIdcardImg: function () {
            var url = vm.shop.handIdcardImg;
            eyeImage(url);
        },
        handleSuccessBusinessLicenseImg: function (res, file) {
            vm.shop.businessLicenseImg = file.response.url;
        },
        eyeImageBusinessLicenseImg: function () {
            var url = vm.shop.businessLicenseImg;
            eyeImage(url);
        },
        eyeImage: function (e) {
            eyeImage($(e.target).attr('src'));
        },
        getProvinces: function(pId){
            Ajax.request({
                url: "../sys/region/getAllProvice?areaId=1",
                async: false,
                successCallback: function (r) {
                    vm.provinces = r.list;
                }
            });
		},
        getCitys: function(pId){
            Ajax.request({
                url: "../sys/region/getAllCity?areaId="+pId,
                async: false,
                successCallback: function (r) {
                    vm.citys = r.list;
                }
            });
		},
        getAreas: function(pId){
            Ajax.request({
                url: "../sys/region/getChildrenDistrict?areaId="+pId,
                async: false,
                successCallback: function (r) {
                    vm.areas = r.list;
                }
            });
		},
		changeProvince:function(opt){
			 var pId = opt.value;
			 vm.$refs.areaCity.clearSingleSelect();
			 vm.$refs.areaDistrict.clearSingleSelect();
			 vm.areas = [];
			 vm.citys = [];
			 if(pId==null||pId==""){
				 return;
			 }
			 vm.getCitys(pId);
			 
		},
		changeCity:function(opt){
			 var pId = opt.value;
			 vm.areas = [];
			 vm.$refs.areaDistrict.clearSingleSelect();
			 if(pId==null||pId==""){
				 return;
			 }
			 vm.getAreas(pId);
		},
        /**
         * 查看详情
         */
        lookDetail: function (id){
        	vm.showList = false;
        	vm.showDetail = true;
		    vm.title="店铺详情";
		    Ajax.request({
		    	url: "../shop/info/"+id,
                async: false,
                successCallback: function (r) {
                	vm.shop = r.shop;
                	console.log(vm.shop)
                	if(vm.shop.shopType){
                		vm.shop.shopType=vm.shop.shopType+"";
                	}
                	if(vm.shop.statu){
                		vm.shop.statu=vm.shop.statu+"";
                	}
                	if(vm.shop.auditStatu){
                		vm.shop.auditStatu=getMacroNameByCode('shopAuditStatus',vm.shop.auditStatu);
                	}
                	if(vm.shop.createTime){
                		vm.shop.createTime=transDate(vm.shop.createTime);
                	}
                	if(vm.shop.updateTime){
                		vm.shop.updateTime=transDate(vm.shop.updateTime);
                	}
                	if(vm.shop.auditTime){
                		vm.shop.auditTime=transDate(vm.shop.auditTime);
                	}
                	vm.getProvinces(0);
                	if(vm.shop.provinceId){
                		vm.getCitys(vm.shop.provinceId);
                	}
                	if(vm.shop.cityId){
                		vm.getAreas(vm.shop.cityId);
                	}
                }
            });
        },
        /**
         * 客户列表选择
         */
        userList: function () {
        	vm.ureloadSearch();
	        openWindow({
	            title: "选择客户",
	            area: ['560px', '350px'],
	            content: jQuery("#userLayer"),
	            btn: ['确定', '取消'],
	            btn1: function (index) {
	            	var grid = $("#userjqGrid");
	                var rowKey = grid.getGridParam("selrow");
	                if (!rowKey) {
	                	layer.alert("请选择一条记录");
	                    return;
	                }

	                var selectedIDs = grid.getGridParam("selarrrow");
	                if (selectedIDs.length > 1) {
	                	layer.alert("只能选择一条记录");
	                    return;
	                }
	            	
	            	var rowData = getSelectedRowData("#userjqGrid");
	                vm.shop.createUserId = rowData.userId;
	                vm.shop.createUserName = rowData.userName;
	                layer.close(index);
	            }
	        });
	    },
	    uquery: function () {
	    	$("#userjqGrid").jqGrid('setGridParam', {
	            datatype:'json'
	        });
			vm.ureload();
		},
		ureload: function (event) {
	        let upage = $("#userjqGrid").jqGrid('getGridParam', 'page');
			$("#userjqGrid").jqGrid('setGridParam', {
	            postData: {'userName': vm.uq.userName},
	            page: upage
	        }).trigger("reloadGrid");
		},
	    ureloadSearch: function() {
	        vm.uq = {
	        		userName: ''
	        }
	        vm.uquery();
	    },
        toAudit: function(id,shopName,auditStatu){
        	vm.shop.id=id;
        	vm.shop.shopName=shopName;
        	vm.shop.auditStatu=auditStatu;
        	vm.shop.auditOpinion="";
        	var btnstr="通过";
        	if(auditStatu=='1'){
        		btnstr="通过";
        	}else if(auditStatu=='2'){
        		btnstr="拒绝";
        	}
        	openWindow({
                title: "店铺申请",
                area: ['400px', '360px'],
                content: jQuery("#auditLayer"),
                btn: [btnstr, '取消'],
                btn1: function (index) {
                	alert(1)
                	if(auditStatu=='1'){
                		vm.saveAudit();
                		layer.close(index);
                	}else if(auditStatu=='2'){
                		layer.confirm('是否拒绝该用户的店铺申请？', {
                			skin: 'layui-layer-molv', 
                			btn: ['是', '否'],
                			yes: function(index1){
                				vm.saveAudit();
                				layer.close(index1);
                				layer.close(index);
                			}
                		});
                	}
                }
            });
        },
        saveAudit:function(){
        	Ajax.request({
			    url: '../shop/saveAudit',
                params: JSON.stringify(vm.shop),
                type: "POST",
                async: false,
			    contentType: "application/json",
                successCallback: function (r) {
                    alert('操作成功', function (index) {
                        vm.reload();
                    });
                }
			});
        }
	}
});