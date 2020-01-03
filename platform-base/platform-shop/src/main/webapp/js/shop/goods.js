$(function () {
    $("#jqGrid").Grid({
        url: '../goods/list',
        colModel: [
			{label: 'id', name: 'id', index: 'id', key: true, hidden: true},
			{label: '商品名称', name: 'name', index: 'name', align: 'center', width: 80},
			{label: '商品编码', name: 'goodsSn', index: 'goods_sn', align: 'center', width: 80},
			{label: '图片', name: 'imgListUrl', index: 'img_list_url', align: 'center', width: 80,formatter: function (value) {
                return transImg(value);
            }},
            {label: '店铺', name: 'shopName', index: 'shop_name', align: 'center', width: 80},
            {label: '厂家', name: 'author', index: 'author', align: 'center', width: 80},
			{label: '商品分类', name: 'categoryName', index: 'category_name', align: 'center', width: 80},
			{label: '单价', name: 'retailPrice', index: 'retail_price', align: 'center', width: 80},
			{label: '库存价', name: 'marketPrice', index: 'market_price', align: 'center', width: 80},
			{label: '上架', name: 'sellStatus', index: 'sell_status', align: 'center', width: 80 ,formatter: function (value) {
                return getMacroNameByCode('sellStatus',value);
            }},
			{label: '创建时间', name: 'createtime', index: 'createtime', align: 'center', width: 80, formatter: function (value) {
                return transDate(value);
            }}
			],
			height:"100%",
            rowNum:50,
    });
    /**
     * 店铺列表，弹出框显示
     */
     $("#shopjqGrid").Grid({
    	 url: '../shop/list',
    	 datatype:'local',
         colModel: [
 			{label: 'id', name: 'id', index: 'id', key: true, hidden: true},
 			{label: '店铺名称', name: 'shopName', index: 'shop_name', width: 240},
 			{label: 'logo', name: 'shopLogo', index: 'shop_logo', width: 240,formatter: function (value) {
                return transImg(value);
            }}
 			]
	});
    $('#goodsDesc').editable({
        inlineMode: false,
        alwaysBlank: true,
        height: '300px', //高度
        minHeight: '200px',
        language: "zh_cn",
        spellcheck: false,
        plainPaste: true,
        enableScript: false,
        imageButtons: ["floatImageLeft", "floatImageNone", "floatImageRight", "linkImage", "replaceImage", "removeImage"],
        allowedImageTypes: ["jpeg", "jpg", "png", "gif"],
        imageUploadURL: '../sys/oss/upload',
        imageUploadParams: {id: "edit"},
        imagesLoadURL: '../sys/oss/queryAll'
    })
    vm.getGoodsCategory();
    vm.getSellStatuss();
});
/**
 * 列表字典翻译中文名称工具方法
 * @param type
 * @param code
 * @returns
 */
function getMacroNameByCode(type,code){
	var macroList=[];
	if(type=="sellStatus"){
		if(vm.sellStatuss.length==0){
			vm.getSellStatuss();
		}
		macroList=vm.sellStatuss;
	}
	for(var i=0;i<macroList.length;i++){
		if(macroList[i].value==code){
			return macroList[i].name;
		}
	}
	return code;
}
var setting = {
	    data: {
	        simpleData: {
	            enable: true,
	            idKey: "id",
	            pIdKey: "parentId",
	            rootPId: -1
	        },
	        key: {
	            url: "nourl",
	            name:"name"
	        }
	    }
	};
	var ztree;
let vm = new Vue({
	el: '#rrapp',
	data: {
        showList: true,
        title: null,
        goods: {
			imgUrl:'',
			imgListUrl:'',
			sellStatus:0,
			isNew:0,
			hotSale:0,
			categoryId:'',
			categoryName:''
		},
		ruleValidate: {
			name: [
				{required: true, message: '商品名称不能为空', trigger: 'blur'}
			],
			categoryName:[
				{required: true, message: '商品分类不能为空', trigger: 'change'}
			]
			,
			author:[
				{required: true, message: '商品作者不能为空', trigger: 'blur'}
			]
			,
			material:[
				{required: true, message: '商品材质不能为空', trigger: 'blur'}
			]
			,
			specification:[
				{required: true, message: '商品规格不能为空', trigger: 'blur'}
			]
		},
		q: {
			name: '',
			goodsSn:'',
			categoryName:'',
			categoryId:'',
			author:'',
			sellStatus:''
		},
		sellStatuss:[],
		uploadList: [],
        imgName: '',
        visible: false,
        /**
		 * 店铺列表查询条件
		 */
		sq:{
			shopName:''
		},
		phq:{
			bidGoodsSn:''
		},
		colorTags:[],
		sizeTags:[],
		otherTags:[],
		curColorTag:'',
		curSizeTag:'',
		curOtherTag:'',
		curUploadUrl:'',
		
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function () {
			vm.showList = false;
			vm.title = "新增";
			vm.uploadList = [];
			vm.goods = {
					imgUrl:'',
					imgListUrl:'',
					sellStatus:'0',
					isNew:'0',
					hotSale:'0',
					categoryId:'',
					categoryName:'',
					marketPrice:0,
					retailPrice:0
			};
			 
			 vm.colorTags=[];
             vm.sizeTags=[];
             vm.otherTags=[];
             
			$('#goodsDesc').editable('setHTML', '');
			//vm.getGoodsCategory();
			vm.getSellStatuss();
		},
		update: function (event) {
            let id = getSelectedRow("#jqGrid");
			if (id == null) {
				return;
			}
			vm.showList = false;
            vm.title = "修改";
            vm.uploadList = [];
            vm.getInfo(id)
            vm.getSellStatuss();
		},
		saveOrUpdate: function (event) {
            let url = vm.goods.id == null ? "../goods/save" : "../goods/update";
            vm.goods.goodsDetail = $('#goodsDesc').editable('getHTML');
            var imgUrls="";
            if(vm.uploadList&&vm.uploadList.length>0){
            	for(var i=0;i<vm.uploadList.length;i++){
            		imgUrls=imgUrls+vm.uploadList[i].imgUrl+","
            	}
            	if(imgUrls&&imgUrls.length>0){
            		imgUrls=imgUrls.substr(0,imgUrls.length-1);
            	}
            }
            vm.goods.images = imgUrls;
            
            vm.goods.imgUrl=vm.goods.imgListUrl;
            
            
            Ajax.request({
			    url: url,
                params: JSON.stringify(vm.goods),
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
				    url: "../goods/delete",
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
                url: "../goods/info/"+id,
                async: true,
                successCallback: function (r) {
                    vm.goods = r.goods;
                    vm.goods.sellStatus = vm.goods.sellStatus+"";
                    if(vm.goods.isNew){
                    	vm.goods.isNew = vm.goods.isNew+"";
                    }else{
                    	vm.goods.isNew='0';
                    }
                    if(vm.goods.hotSale){
                    	vm.goods.hotSale = vm.goods.hotSale+"";
                    }else{
                    	vm.goods.hotSale='0';
                    }
                    $('#goodsDesc').editable('setHTML', vm.goods.goodsDetail);
                    
                    if(vm.goods.images&&vm.goods.images.length>0){
                    	var imgUrls = vm.goods.images.split(',');
                    	if(imgUrls&&imgUrls.length>0){
                    		for(var i=0;i<imgUrls.length;i++){
                    			var imgobj={};
                    			imgobj.imgUrl=imgUrls[i];
                    			imgobj.name=imgUrls[i];
                    			imgobj.status='finished';
                    			vm.uploadList.add(imgobj);
                    		}
                    	}
                    console.log(vm.uploadList)	 
                    }
                    vm.colorTags=[];
                    vm.sizeTags=[];
                    vm.otherTags=[];
                    
                    if(vm.goods.goodColorTag){
                    	var goodColorTag=vm.goods.goodColorTag;
                    	vm.colorTags=goodColorTag.split(',');
                    	
                    }
                    if(vm.goods.goodSizeTag){
                    	var goodSizeTag=vm.goods.goodSizeTag;
                    	vm.sizeTags=goodSizeTag.split(',');
                    }
                    if(vm.goods.goodOtherTag){
                    	var goodOtherTag=vm.goods.goodOtherTag;
                    	vm.otherTags=goodOtherTag.split(',');
                    }
                }
            });
		},
		reload: function (event) {
			vm.showList = true;
            let page = $("#jqGrid").jqGrid('getGridParam', 'page');
			$("#jqGrid").jqGrid('setGridParam', {
                postData: {'name': vm.q.name,
                	author:vm.q.author,
                	'goodsSn': vm.q.goodsSn,
                	'categoryId':vm.q.categoryId,'sellStatus':vm.q.sellStatus},
                page: page
            }).trigger("reloadGrid");
            vm.handleReset('formValidate');
		},
        reloadSearch: function() {
            vm.q = {
        			name: '',
        			goodsSn:'',
        			categoryId:'',
        			author:'',
        			categoryName:'',
        			sellStatus:''
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
            vm.goods = {
    				sellStatus:'0',
    				isNew:'0',
    				hotSale:'0',
    				marketPrice:0,
					retailPrice:0
    			};
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
        handleSuccessImgUrl: function (res, file) {
            vm.goods.imgUrl = file.response.url;
        },
        handleSuccessImgListUrl: function (res, file) {
            vm.goods.imgListUrl = file.response.url;
        },
        eyeImageImgUrl: function () {
            var url = vm.goods.imgUrl;
            eyeImage(url);
        },
        eyeImageImgListUrl: function () {
            var url = vm.goods.imgListUrl;
            eyeImage(url);
        },
        eyeImage: function (e) {
            eyeImage($(e.target).attr('src'));
        },
        handleView(name) {
            vm.imgName = name;
            vm.visible = true;
        },
        handleRemove(file) {
            // 从 upload 实例删除数据
            const fileList = vm.uploadList;
            vm.uploadList.splice(fileList.indexOf(file), 1);
        },
        handleSuccess(res, file) {
            // 因为上传过程为实例，这里模拟添加 url
            file.imgUrl = res.url;
            file.name = res.url;
            vm.uploadList.add(file);
        },
        addUploadFile:function(){
        	var file={};
        	file.imgUrl = vm.curUploadUrl;
            file.name = vm.curUploadUrl;
            file.status = 'finished';
            vm.uploadList.add(file);
            vm.curUploadUrl="";
        },
        handleBeforeUpload() {
            const check = vm.uploadList.length < 5;
            if (!check) {
                this.$Notice.warning({
                    title: '最多只能上传 5 张图片。'
                });
            }
            return check;
        },
		goodsCategoryTree: function () {
            openWindow({
                title: "选择类别",
                area: ['300px', '450px'],
                content: jQuery("#goodsCategoryLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = ztree.getSelectedNodes();
                    if(node[0].id==0){
                    	layer.alert("不能选择一级类别！");
                    	return;
                    }
                    vm.goods.categoryId = node[0].id;
                    vm.goods.categoryName = node[0].name;
                    layer.close(index);
                }
            });
        },
		QgoodsCategoryTree: function () {
            openWindow({
                title: "选择类别",
                area: ['300px', '450px'],
                content: jQuery("#goodsCategoryLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var node = ztree.getSelectedNodes();
                    if(node[0].id==0){
                    	layer.alert("不能选择一级类别！");
                    	return;
                    }
                    vm.q.categoryId = node[0].id;
                    vm.q.categoryName = node[0].name;
                    layer.close(index);
                }
            });
        },
        getGoodsCategory: function () {
            //加载菜单树
            Ajax.request({
                url: "../goodscategory/select?type=goods",
                async: true,
                successCallback: function (r) {
                    ztree = $.fn.zTree.init($("#goodsCategoryTree"), setting, r.goodsCategoryList);
                    var node = ztree.getNodeByParam("categoryId", vm.goods.categoryId);
                    if (node) {
                        ztree.selectNode(node);
                        vm.goods.categoryName = node.name;
                    }
                }
            });
        },
        enSale: function () {
            var id = getSelectedRow("#jqGrid");
            if (id == null) {
                return;
            }
            confirm('确定要上架选中的商品？', function () {
                Ajax.request({
                    type: "POST",
                    url: "../goods/enSale",
                    params: JSON.stringify(id),
                    contentType: "application/json",
                    type: 'POST',
                    successCallback: function () {
                        alert('提交成功', function (index) {
                            vm.reload();
                        });
                    }
                });
            });
        },
        unSale: function () {
            var id = getSelectedRow("#jqGrid");
            if (id == null) {
                return;
            }
            confirm('确定要下架选中的商品？', function () {

                Ajax.request({
                    type: "POST",
                    url: "../goods/unSale",
                    contentType: "application/json",
                    params: JSON.stringify(id),
                    successCallback: function (r) {
                        alert('操作成功', function (index) {
                            vm.reload();;
                        });
                    }
                });

            });
        },
        /**
         * 支付状态
         */
        getSellStatuss: function () {
        	if(vm.sellStatuss.length>0){
        		return;
        	}
            Ajax.request({
                url: "../sys/macro/queryMacrosByValue?value=goodsSellStatus",
                async: false,
                successCallback: function (r) {
                    vm.sellStatuss = r.list;
                }
            });
        },
        
        /**
         * 店铺列表选择
         */
        shopList: function () {
        	vm.sreloadSearch();
            openWindow({
                title: "选择店铺",
                area: ['560px', '350px'],
                content: jQuery("#shopLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                	var grid = $("#shopjqGrid");
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
                	
                	var rowData = getSelectedRowData("#shopjqGrid");
                    vm.goods.shopId = rowData.id;
                    vm.goods.shopName = rowData.shopName;
                    layer.close(index);
                }
            });
        },
        squery: function () {
        	$("#shopjqGrid").jqGrid('setGridParam', {
                datatype:'json'
            });
    		vm.sreload();
    	},
    	sreload: function (event) {
            let spage = $("#shopjqGrid").jqGrid('getGridParam', 'page');
    		$("#shopjqGrid").jqGrid('setGridParam', {
                postData: {'shopName': vm.sq.shopName},
                page: spage
            }).trigger("reloadGrid");
    	},
        sreloadSearch: function() {
            vm.sq = {
            		shopName: ''
            }
            vm.squery();
        },
        deleteColorTag:function(event, name){
        	var tags=vm.colorTags;
        	for(var i=0;i<tags.length;i++){
        		if(tags[i]==name){
        			tags.splice(i, 1);
        		}
        	}
        	vm.colorTags=tags;
        	vm.goods.goodColorTag=tags.join(",");
        },
        deleteSizeTag:function(event, name){
        	var tags=vm.sizeTags;
        	for(var i=0;i<tags.length;i++){
        		if(tags[i]==name){
        			tags.splice(i, 1);
        		}
        	}
        	vm.sizeTags=tags;
        	vm.goods.goodSizeTag=tags.join(",");
        },
        deleteOtherTag:function(event, name){
        	var tags=vm.otherTags;
        	for(var i=0;i<tags.length;i++){
        		if(tags[i]==name){
        			tags.splice(i, 1);
        		}
        	}
        	vm.otherTags=tags;
        	vm.goods.goodOtherTag=tags.join(",");
        },
        
        
        
        addSizeTag:function(event, name){
           if(vm.curSizeTag&&vm.curSizeTag!=''){
        	vm.sizeTags.push(vm.curSizeTag);
        	vm.goods.goodSizeTag=vm.sizeTags.join(",");
        	vm.curSizeTag="";
        	}
        },
        addColorTag:function(event, name){
        	if(vm.curColorTag&&vm.curColorTag!=''){
        	vm.colorTags.push(vm.curColorTag);
        	vm.goods.goodColorTag=vm.colorTags.join(",");
        	vm.curColorTag="";
        	}
        },
        addOtherTag:function(event, name){
        	if(vm.curOtherTag&&vm.curOtherTag!=''){
        		vm.otherTags.push(vm.curOtherTag);
            	vm.goods.goodOtherTag=vm.otherTags.join(",");
            	vm.curOtherTag="";	
        	}
        	
        },
        
        
        
	},
    mounted() {
        this.uploadList = this.$refs.upload.fileList;
    }
});