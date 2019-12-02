$(function () {
    $("#jqGrid").Grid({
        url: '../useraudit/list',
        colModel: [
			{label: 'id', name: 'id', index: 'id', key: true, hidden: true},
			{label: '申请人ID', name: 'applyUserId', index: 'apply_user_id', width: 80, hidden: true},
			{label: '用户名', name: 'applyUserName', index: 'applyUserName', width: 80},
			{label: '申请日期', name: 'applyTime', index: 'apply_time', width: 80, formatter: function (value) {
                return transDate(value);
            }},
			{label: '申请等级', name: 'applyLevel', index: 'apply_level', width: 60, formatter: function (value) {
				 return getMacroNameByCode('userNamedType',value);
            }},
			{label: '当前等级', name: 'nowLevel', index: 'now_level', width: 60, formatter: function (value) {
				return getMacroNameByCode('userNamedType',value);
            }},
			{label: '线下打款', name: 'payAmount', index: 'pay_amount', width: 80},
			{label: '审核状态', name: 'auditStatus', index: 'audit_status', width: 100, formatter: function (value, col, row) {
				if(value == '1'){
					var btnstr='<button class="btn btn-outline btn-primary" onclick="vm.toAudit(\'' + row.id +'\',\''+ row.applyUserId +'\',\'' + row.applyUserName +'\',\'' + row.applyLevel +'\',\'' + row.nowLevel +'\',\'2\')"><i class="fa fa-info-circle"></i>&nbsp;通过</button>';
					btnstr+='&nbsp;<button class="btn btn-outline btn-warning" onclick="vm.toAudit(\'' + row.id +'\',\''+ row.applyUserId +'\',\'' + row.applyUserName +'\',\'' + row.applyLevel +'\',\'' + row.nowLevel +'\',\'3\')"><i class="fa fa-info-circle"></i>&nbsp;拒绝</button>';
					return btnstr;
				}else if(value=='2'){
					return '<span class="label label-success">'+getMacroNameByCode('auditStatus',value)+'</span>';
				}else if(value=='3'){
					return '<span class="label label-danger">'+getMacroNameByCode('auditStatus',value)+'</span>';
				}else{
					return '-'+value+'-';
				}
            }},
			{label: '申请类型', name: 'applyType', index: 'apply_type', width: 80, hidden: true},
			{label: '审核人ID', name: 'auditUserId', index: 'audit_user_id', width: 80, hidden: true},
			{label: '审核时间', name: 'auditTime', index: 'audit_time', width: 80, hidden: true},
			{label: '审核意见', name: 'auditOpinion', index: 'audit_opinion', width: 80, hidden: true},
			
            {label: '操作', width: 80, align: 'center', sortable: false, formatter: function (value, col, row) {
                return '<button class="btn btn-outline btn-info" onclick="vm.toAddIntegral(\'' + row.applyUserId +'\',\'' + row.applyUserName +'\')"><i class="fa fa-info-circle"></i>&nbsp;转积分</button>';
            }}],
            height:"100%",
            rowNum:50,
    });
    vm.getAuditStatuss();
});
/**
 * 列表字典翻译中文名称工具方法
 * @param type
 * @param code
 * @returns
 */
function getMacroNameByCode(type,code){
	var macroList=[];
	if(type=="auditStatus"){//审核状态
		if(vm.auditStatuss.length==0){
			vm.getAuditStatuss();
		}
		macroList=vm.auditStatuss;
	}else if(type=="userNamedType"){
		if(vm.userNamedTypes.length==0){
			vm.getUserNamedTypes();
		}
		macroList=vm.userNamedTypes;
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
        title: null,
		userAudit: {
			id:'',
			applyUserId:'',
			applyUserName:'',
			applyLevel:'',
			applyLevelName:'',
			nowLevel:'',
			nowLevelName:'',
			payAmount:0,
			auditStatus:'',
			auditOpinion:''
		},
		ruleValidate: {
			name: [
				{required: true, message: '名称不能为空', trigger: 'blur'}
			]
		},
		q: {
			applyUserName:'',
			auditStatus: '',
			queryDate:[]
		},
		auditStatuss:[],
		userNamedTypes:[],
		addIntegerUser:{
			userId:'',
			userName:'',
			integralScore:1
		}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		/*add: function () {
			vm.showList = false;
			vm.title = "新增";
			vm.userAudit = {};
		},
		update: function (event) {
            let id = getSelectedRow("#jqGrid");
			if (id == null) {
				return;
			}
			vm.showList = false;
            vm.title = "修改";

            vm.getInfo(id)
		},*/
		saveOrUpdate: function (event) {
            let url = vm.userAudit.id == null ? "../useraudit/save" : "../useraudit/update";
            Ajax.request({
			    url: url,
                params: JSON.stringify(vm.userAudit),
                type: "POST",
			    contentType: "application/json",
                successCallback: function (r) {
                    alert('操作成功', function (index) {
                        vm.reload();
                    });
                }
			});
		},
		/*del: function (event) {
            let ids = getSelectedRows("#jqGrid");
			if (ids == null){
				return;
			}

			confirm('确定要删除选中的记录？', function () {
                Ajax.request({
				    url: "../useraudit/delete",
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
                url: "../useraudit/info/"+id,
                async: true,
                successCallback: function (r) {
                    vm.userAudit = r.userAudit;
                }
            });
		},*/
		reload: function (event) {
			vm.showList = true;
			var confirmTimeStart='';
		    var confirmTimeEnd='';
		    if(vm.q.queryDate&&vm.q.queryDate.length==2&&vm.q.queryDate[0]&&vm.q.queryDate[1]){
		    	confirmTimeStart=vm.q.queryDate[0].dateFormat('yyyy-MM-dd 00:00:00');
		    	confirmTimeEnd=vm.q.queryDate[1].dateFormat('yyyy-MM-dd 23:59:59');
		    }
            let page = $("#jqGrid").jqGrid('getGridParam', 'page');
			$("#jqGrid").jqGrid('setGridParam', {
                postData: {
                	'applyUserName': vm.q.applyUserName,
                	'auditStatus':vm.q.auditStatus,
                	'confirmTimeStart':confirmTimeStart,
                	'confirmTimeEnd':confirmTimeEnd
                	},
                page: page
            }).trigger("reloadGrid");
            vm.handleReset('formValidate');
		},
        reloadSearch: function() {
            vm.q = {
            		applyUserName: '',
            		auditStatus:'',
        			queryDate:[]
            }
            vm.addIntegerUser={
            		userId:'',
        			userName:'',
        			integralScore:1
            }
            vm.userAudit={
        			id:'',
        			applyUserId:'',
        			applyUserName:'',
        			applyLevel:'',
        			applyLevelName:'',
        			nowLevel:'',
        			nowLevelName:'',
        			payAmount:0,
        			auditStatus:'',
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
         * 审核状态
         */
        getAuditStatuss: function () {
        	if(vm.auditStatuss.length>0){
        		return;
        	}
            Ajax.request({
                url: "../sys/macro/queryMacrosByValue?value=auditStatus",
                async: false,
                successCallback: function (r) {
                    vm.auditStatuss = r.list;
                }
            });
        },
        /**
         * 经销商级别
         */
        getUserNamedTypes: function () {
            Ajax.request({
                url: "../sys/macro/queryMacrosByValue?value=userNamedType",
                async: false,
                successCallback: function (r) {
                    vm.userNamedTypes = r.list;
                }
            });
        },
        toAddIntegral: function(userId,userName){
        	console.log(userId,userName)
        	vm.addIntegerUser.userId=userId;
        	vm.addIntegerUser.userName=userName;
        	vm.addIntegerUser.integralScore=1;
        	console.log(vm.addIntegerUser)
        	openWindow({
                title: "转积分",
                area: ['300px', '230px'],
                content: jQuery("#addIntegralLayer"),
                btn: ['转积分', '取消'],
                btn1: function (index) {
                    layer.confirm('是否确认为该用户【'+vm.addIntegerUser.userName+'】转出【'+vm.addIntegerUser.integralScore+'】积分？', {
	            		skin: 'layui-layer-molv', 
	            		btn: ['是', '否'],
	            		yes: function(index1){
	            			vm.addIntegral();
	                		layer.close(index1);
	            			layer.close(index);
	            		}
	            	});
                }
            });
        },
        addIntegral:function(){
        	Ajax.request({
			    url: '../user/addIntegral',
                params: JSON.stringify(vm.addIntegerUser),
                type: "POST",
                async: false,
			    contentType: "application/json",
                successCallback: function (r) {
                    alert('操作成功', function (index) {
                        vm.reload();
                    });
                }
			});
        },
        toAudit: function(id,applyUserId,applyUserName,applyLevel,nowLevel,auditStatus){
        	vm.userAudit.id=id;
        	vm.userAudit.applyUserId=applyUserId;
        	vm.userAudit.applyUserName=applyUserName;
        	vm.userAudit.applyLevelName=getMacroNameByCode('userNamedType',applyLevel);
        	vm.userAudit.nowLevelName=getMacroNameByCode('userNamedType',nowLevel);
        	vm.userAudit.auditStatus=auditStatus;
        	vm.userAudit.payAmount=0;
        	vm.userAudit.auditOpinion="";
        	var btnstr="通过";
        	if(auditStatus=='2'){
        		btnstr="通过";
        	}else if(auditStatus=='3'){
        		btnstr="拒绝";
        	}
        	openWindow({
                title: "升级申请",
                area: ['400px', '360px'],
                content: jQuery("#auditLayer"),
                btn: [btnstr, '取消'],
                btn1: function (index) {
                	if(auditStatus=='2'){
                		vm.saveAudit();
                		layer.close(index);
                	}else if(auditStatus=='3'){
                		layer.confirm('是否拒绝该用户的升级申请？', {
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
			    url: '../useraudit/saveAudit',
                params: JSON.stringify(vm.userAudit),
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