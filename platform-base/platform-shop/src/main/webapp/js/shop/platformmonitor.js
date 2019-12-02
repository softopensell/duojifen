$(function () {
    $("#jqGrid").Grid({
        url: '../platformmonitor/list',
        colModel: [
			{label: 'id', name: 'id', index: 'id', key: true, hidden: true},
			{label: '监控序号', name: 'monitorDateNumber', index: 'monitor_date_number', width: 80,align : "center"},
			{label: '统计类型', name: 'monitorType', index: 'monitor_type', width: 80,align : "center"},
			{label: '数量', name: 'monitorUserAbnormalSum', index: 'monitor_user_abnormal_sum', width: 80,align : "center"},
			{label: '时间', name: 'monitorDate', index: 'monitor_date', width: 120,align : "center", formatter: function (value) {
	                return transDate(value);
	            }},
	        {label: '操作', width: 80, align: 'center', sortable: false, formatter: function (value, col, row) {
	              	 var operationStr='';
	              	 operationStr=operationStr+'<button class="btn btn-outline btn-primary btn-sm" onclick="vm.toShowDetail(\'' + row.monitorDateNumber +'\')"><i class="fa fa-info-circle"></i>&nbsp;查看</button>';
	              	 return operationStr;
	                }}
			]
    });
});

let vm = new Vue({
	el: '#rrapp',
	data: {
        showList: true,
        title: null,
		platformMonitor: {},
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
			vm.platformMonitor = {};
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
            let url = vm.platformMonitor.id == null ? "../platformmonitor/save" : "../platformmonitor/update";
            Ajax.request({
			    url: url,
                params: JSON.stringify(vm.platformMonitor),
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
				    url: "../platformmonitor/delete",
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
                url: "../platformmonitor/info/"+id,
                async: true,
                successCallback: function (r) {
                    vm.platformMonitor = r.platformMonitor;
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
        toShowDetail: function (monitorDateNumber) {
        	openWindow({
        		area: ['95%', '90%'],
        		type: 2,
        		top:true,
        		title: '<i class="fa fa-print"></i>问题列表',
        		content: 'shop/platformmonitorstadetail.html?monitorDateNumber=' + monitorDateNumber
        	})
        },
        testLock:function(){
        	 
        	
//        	 for(var i=0;i<10;i++){
//        		 Ajax.request({
//                     url: "../api/index/testLock?userId=1",
//                     async: true,
//                     successCallback: function (r) {
//                     }
//                 });
//        	 }
//        	 
//        	 for(var i=0;i<10;i++){
//        		 Ajax.request({
//                     url: "../api/index/testNoLock?userId=2",
//                     async: true,
//                     successCallback: function (r) {
//                     }
//                 });
//        	 }
        	 
        }
	}
});