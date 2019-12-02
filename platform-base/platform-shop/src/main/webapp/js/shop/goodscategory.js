$(function () {
    initialPage();
    getGrid();
});

function initialPage() {
    $(window).resize(function () {
        TreeGrid.table.resetHeight({height: $(window).height() - 100});
    });
}

function getGrid() {
    var colunms = TreeGrid.initColumn();
    var table = new TreeTable(TreeGrid.id, '../goodscategory/queryAll', colunms);
    table.setExpandColumn(2);
    table.setIdField("id");
    table.setCodeField("id");
    table.setParentCodeField("parentId");
    table.setExpandAll(false);
    table.setHeight($(window).height() - 100);
    table.init();
    TreeGrid.table = table;
}

var TreeGrid = {
    id: "jqGrid",
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
TreeGrid.initColumn = function () {
    var columns = [
        {field: 'selectItem', radio: true},
        {title: '类别ID', field: 'id', align: 'id', width: '50px'},
        {title: '类别名称', field: 'name', align: 'center', valign: 'middle', width: '150px'},
        {title: '类别编码', field: 'code', align: 'center', valign: 'middle', width: '100px'},
        {title: '父级类别', field: 'parentName', align: 'center', valign: 'middle', width: '150px'}
        ]
    return columns;
};
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
		goodsCategory: {
			bannerImg:'',
			imgLinkUrl:'',
			sortOrder:0,
			statu:'0',
			isShow:'1',
			parentName: null,
            parentId: 0
            },
		ruleValidate: {
			name: [
				{required: true, message: '类别名称不能为空', trigger: 'blur'}
			],
			code: [
				{required: true, message: '类别编码不能为空', trigger: 'blur'}
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
			var ids = TreeGrid.table.getSelectedRow();
			var parentId = 0;
			if (ids.length != 0) {
				parentId = ids[0].id;
			}
			vm.goodsCategory = {
				bannerImg:'',
				imgLinkUrl:'',
				sortOrder:0,
				statu:'0',
				isShow:'1',
				parentName: null,
	            parentId: 0
	        };
			vm.getGoodsCategory();
		},
		update: function (event) {
			var ids = TreeGrid.table.getSelectedRow();
            if (ids.length == 0) {
                iview.Message.error("请选择一条记录");
                return;
            }
			vm.showList = false;
            vm.title = "修改";
            vm.getInfo(ids[0].id);
		},
		saveOrUpdate: function (event) {
            let url = vm.goodsCategory.id == null ? "../goodscategory/save" : "../goodscategory/update";
            Ajax.request({
			    url: url,
                params: JSON.stringify(vm.goodsCategory),
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
			 var id = TreeGrid.table.getSelectedRow(), ids = [];
	            if (id.length == 0) {
	                iview.Message.error("请选择一条记录");
	                return;
	            }
	            $.each(id, function (idx, item) {
	                ids[idx] = item.id;
	            });

			confirm('确定要删除选中的记录？', function () {
                Ajax.request({
				    url: "../goodscategory/delete",
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
                url: "../goodscategory/info/"+id,
                async: true,
                successCallback: function (r) {
                    vm.goodsCategory = r.goodsCategory;
                    if(vm.goodsCategory.statu){
                    	vm.goodsCategory.statu=vm.goodsCategory.statu+"";
                    }else{
                    	vm.goodsCategory.statu="0";
                    }
                    if(vm.goodsCategory.isShow){
                    	vm.goodsCategory.isShow=vm.goodsCategory.isShow+"";
                    }else{
                    	vm.goodsCategory.isShow="1";
                    }
                    vm.getGoodsCategory();
                }
            });
		},
		goodsCategoryTree: function () {
            openWindow({
                title: "选择类别",
                area: ['300px', '450px'],
                content: jQuery("#goodsCategoryLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                	console.log(vm.goodsCategory);
                    var node = ztree.getSelectedNodes();
                    console.log(node);
                    //选择上级菜单
                    vm.goodsCategory.parentId = node[0].id;
                    vm.goodsCategory.parentName = node[0].name;
                    console.log(vm.goodsCategory);
                    layer.close(index);
                }
            });
        },
        getGoodsCategory: function () {
            //加载菜单树
            Ajax.request({
                url: "../goodscategory/select?type=category",
                async: true,
                successCallback: function (r) {
                    ztree = $.fn.zTree.init($("#goodsCategoryTree"), setting, r.goodsCategoryList);
                    var node = ztree.getNodeByParam("id", vm.goodsCategory.parentId);
                    if (node) {
                        ztree.selectNode(node);
                        vm.goodsCategory.parentName = node.name;
                    } else {
                        node = ztree.getNodeByParam("id", 0);
                        ztree.selectNode(node);
                        vm.goodsCategory.parentName = node.name;
                    }
                }
            });
        },
		reload: function (event) {
			vm.showList = true;
			TreeGrid.table.refresh();
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
        handleSuccessIconImg: function (res, file) {
            vm.goodsCategory.iconImg = file.response.url;
        },
        handleSuccessBannerImg: function (res, file) {
            vm.goodsCategory.bannerImg = file.response.url;
        },
        eyeImageIconImg: function () {
            var url = vm.goodsCategory.iconImg;
            eyeImage(url);
        },
        eyeImageBannerImg: function () {
            var url = vm.goodsCategory.bannerImg;
            eyeImage(url);
        },
        eyeImage: function (e) {
            eyeImage($(e.target).attr('src'));
        }
	}
});