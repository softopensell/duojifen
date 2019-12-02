$(function () {
    $("#jqGrid").Grid({
        url: '../activityitem/list',
        colModel: [
			{label: 'id', name: 'id', index: 'id', key: true, hidden: true},
			{label: '活动号', name: 'itemNo', index: 'item_no', width: 80},
			{label: '标题', name: 'title', index: 'title', align: 'center',width: 80},
			{label: '主办方', name: 'organizerName', index: 'organizer_name',align: 'center', width: 80},
			{label: '开始时间', name: 'startTime', index: 'start_time', width: 80,align: 'center', formatter: function (value) {
                return transDate(value);
            }},
            {label: '结束时间', name: 'endTime', index: 'end_time', width: 80,align: 'center', formatter: function (value) {
                return transDate(value);
            }},
//			{label: '分类', name: 'itemKind', index: 'item_kind', width: 80},
//			{label: '类型', name: 'itemType', index: 'item_type', width: 80},
			{label: '申请人数', name: 'applyPeopleSum', index: 'apply_people_sum',align: 'center', width: 80},
			{label: '顺序', name: 'itemSequence', index: 'item_sequence',align: 'center', width: 80},
			{label: '发布状态', width: 80, align: 'center', sortable: false,align : "center", formatter: function (value, col, row) {
            	if(parseInt(row.statu)==0){
	           		 return '<span class="badge badge-danger">未发布</span>';
	           	 }else if(parseInt(row.statu)==1){
	           		 return '<span class="badge badge-danger">未发布</span>';
	           	 }else if(parseInt(row.statu)==2){
	           		 return '<span class="badge badge-danger">已发布</span>';
	           	 }else if(parseInt(row.statu)==3){
	           		 return '<span class="badge badge-danger">已下线</span>';
	           	 }else if(parseInt(row.statu)==10){
	           		 return '<span class="badge badge-danger">已删除</span>';
	           	 }
	           	 return '无'; 
            }},
            {label: '报名方式', width: 80, align: 'center', sortable: false,align : "center", formatter: function (value, col, row) {
            	if(parseInt(row.itemPayType)==0){
            		return '现金';
            	}else if(parseInt(row.itemPayType)==1){
            		return '积分';
            	}else if(parseInt(row.itemPayType)==2){
            		return '奖励资产';
            	}
            	return '-'; 
            }},
            {label: '审核状态', width: 80, align: 'center', sortable: false,align : "center", formatter: function (value, col, row) {
            	if(parseInt(row.itemAuditStatu)==0){
              		 return '<span class="badge badge-danger">还未审核</span>';
              	 }else if(parseInt(row.itemAuditStatu)==1){
              		 return '<span class="badge badge-danger">已经申请</span>';
              	 }else if(parseInt(row.itemAuditStatu)==2){
              		 return '<span class="badge badge-danger">审核通过</span>';
              	 }else if(parseInt(row.itemAuditStatu)==3){
              		 return '<span class="badge badge-danger">审核拒绝</span>';
              	 }else if(parseInt(row.itemAuditStatu)==4){
              		 return '<span class="badge badge-danger">重新审核</span>';
              	 }
              	 return '无'; 
            }},
			{label: '时间', name: 'updateTime', index: 'update_time', align: 'center',width: 80, formatter: function (value) {
                return transDate(value);
            }},
			{label: '操作', width: 80, align: 'center', sortable: false,align : "center", formatter: function (value, col, row) {
            	var operationStr="";
            	 operationStr=operationStr+'<button class="btn  btn-danger" onclick="vm.modActivityItem(\'' + row.id +'\')">修改</button><br/>';
            	 operationStr=operationStr+ '<button class="btn  btn-info" onclick="vm.toAudit(\'' + row.id +'\')">审核</button>';
            	return operationStr;
            }},
		]
    });
    
    $('#activityDesc').editable({
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
});

let vm = new Vue({
	el: '#rrapp',
	data: {
        showList: true,
        title: null,
		activityItem: {},
		ruleValidate: {
			name: [
				{required: true, message: '名称不能为空', trigger: 'blur'}
			]
		},
		q: {
		    name: ''
		},
		showAuditActivity:false,
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function () {
			vm.showList = false;
			vm.title = "新增";
			vm.activityItem = {};
			$('#activityDesc').editable('setHTML', '');
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
            let url = vm.activityItem.id == null ? "../activityitem/save" : "../activityitem/update";
            vm.activityItem.description = $('#activityDesc').editable('getHTML');
            console.log(JSON.stringify(vm.activityItem));
            Ajax.request({
			    url: url,
                params: JSON.stringify(vm.activityItem),
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
				    url: "../activityitem/delete",
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
                url: "../activityitem/info/"+id,
                async: true,
                successCallback: function (r) {
                    vm.activityItem = r.activityItem;
                    if(vm.activityItem.description){
                    	$('#activityDesc').editable('setHTML', vm.activityItem.description);
                    }
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
            vm.activityItem.itemLogo = file.response.url;
        },
        eyeImageImgUrl: function () {
            var url = vm.activityItem.itemLogo;
            eyeImage(url);
        },
        eyeImage: function (e) {
            eyeImage($(e.target).attr('src'));
        },
        toAudit:function(id){
        	vm.showAuditActivity=true;
        	vm.getInfo(id);
        },
        modActivityItem:function(id){
        	vm.showList = false;
            vm.title = "修改";
            vm.getInfo(id);
        },
	}
});