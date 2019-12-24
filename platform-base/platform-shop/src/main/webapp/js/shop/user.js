$(function () {
    $("#jqGrid").Grid({
        url: '../user/list',
        colModel: [
			{label: 'userId', name: 'userId', index: 'user_id', key: true, hidden: true},
			{label: '账号', name: 'userName', index: 'user_name', width: 80,align : "center"},
			{label: '昵称', name: 'nickname', index: 'nickname', width: 80,align : "center"},
			{label: '级别', name: 'userLevelTypeName', index: 'user_level_type', width: 40,align : "center"},
			{label: '电话', name: 'mobile', index: 'mobile', width: 100,align : "center"},
			{label: '推荐会员', name: 'signupInvitedPhone', width: 80,align : "center"},
			{label: '父接点', name: 'signupNodePhone', width: 80,align : "center"},
			{label: '团队资产', name: 'bonusTeamInvitedPoints', width: 80,align : "center"},
            {label: 'USDT余额', name: 'balance', index: 'balance', width: 80,align : "center"},
            {label: '积分', name: 'integralScore', index: 'integral_score', width: 80,align : "center"},
            {label: '总兑换金额', name: 'totalInvestMoney', index: 'total_invest_money', width: 80,align : "center"},
            {label: '总资产', name: 'totalInvestIncomeMoney', index: 'total_invest_income_money', width: 80,align : "center"},
            {label: '已收益', name: 'investIncomeMoney', index: 'invest_income_money', width: 80,align : "center"},
            {label: '剩余资产', name: 'surplusInvestMoney', index: 'surplus_invest_money', width: 80,align : "center"},
            {label: '基金', name: 'fund', index: 'fund', width: 80,align : "center"},
            {label: '上次奖励时间', name: 'shareInvestLastTime', index: 'share_invest_last_time', width: 120,align : "center", formatter: function (value) {
                return transDate(value);
            }},
            {
                label: '状态', name: 'state', index: 'state', width: 80,align : "center", formatter: function (value,col,row) {
                	if(value==0){
    					return ' <button class="btn btn-outline btn-sm">注册中</button>';
    				}else if(value==1){
    					return ' <button class="btn btn-default btn-sm">有效</button>';
    				} else if(value==2){
    					return ' <button class="btn btn-warning btn-sm">失败</button>';
    				} else if(value==3){
    					return '<button class="btn btn-warning btn-sm">停止分红（失效）</button>';
    				} 
                }
            },
			{label: '注册时间', name: 'registerTime', index: 'register_time',align : "center", width: 80, formatter: function (value) {
                return transDate(value);
            }},
//			{label: '最后登录时间', name: 'lastLoginTime', index: 'last_login_time', width: 80, formatter: function (value) {
//                return transDate(value);
//            }},
            {label: 'A区', name: 'invitedRightUserId', index: 'invitedRightUserId', width: 80,align : "center",formatter: function (value) {
				if(value==null){
					return "";
				}else{
					return "是";
				}
            }},
            {label: 'B区', name: 'invitedUserId', index: 'invitedUserId', width: 80,align : "center",formatter: function (value) {
				if(value==null){
					return "";
				}else{
					return "是";
				}
            }},
//            {label: '服务中心', name: 'fwName', index: 'fwName', width: 80,align : "center"},
            {label: '操作', align: 'center', sortable: false,align : "center",width: 200, formatter: function (value, col, row) {
            	 var operationStr='';
                 operationStr=operationStr+'<button class="btn btn-outline btn-danger btn-sm" onclick="vm.showInfo(\'' + row.userId +'\')"><i class="fa fa-info-circle"></i>&nbsp;查看</button>';
            	 operationStr=operationStr+'<button class="btn btn-outline btn-danger btn-sm" onclick="vm.showRecommondTree(\'' + row.userId +'\')">推荐树</button><br/>';
            	 operationStr=operationStr+'<button class="btn btn-outline btn-danger btn-sm" onclick="vm.showNodeTree(\'' + row.userId +'\')">节点树</button>';
            	 operationStr=operationStr+'<button class="btn btn-outline btn-danger btn-sm" onclick="vm.showUserStat(\'' + row.userId +'\')">查看对账</button><br/>';
            	 operationStr=operationStr+'<button class="btn btn-outline btn-danger btn-sm" onclick="vm.showModNode(\'' + row.userId +'\')">更新节点</button>';
            	 operationStr=operationStr+'<button class="btn btn-outline btn-danger btn-sm" onclick="vm.showUserCenter(\'' + row.userId +'\')">服务中心</button><br/>';
            	 return operationStr;
            }}],
            height:"100%",
            rowNum:50,
//			shrinkToFit: false,
//			autoScroll: true,          //shrinkToFit: false,autoScroll: true,这两个属性产生水平滚动条   
//	        autowidth: true,          //必须要,否则没有水平滚动条
    });
});
/**
 * 列表字典翻译中文名称工具方法
 * @param type
 * @param code
 * @returns
 */
function getMacroNameByCode(type,code){
	if(code==null){
		code="";
	}
	var macroList=[];
	if(type=="userSex"){
		if(vm.userSexs.length==0){
			vm.getUserSexs();
		}
		macroList=vm.userSexs;
	}else if(type=="userStatus"){
		if(vm.userStatuss.length==0){
			vm.getUserStatuss();
		}
		macroList=vm.userStatuss;
	}else if(type=="userRoleType"){
		if(vm.userRoleTypes.length==0){
			vm.getUserRoleTypes();
		}
		macroList=vm.userRoleTypes;
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

//用于捕获异步加载之前的事件回调函数，zTree 根据返回值确定是否允许进行异步加载
function zTreeBeforeAsync(treeId, treeNode) {
    //alert("这是异步加载之前的事件回调函数");
};
//异步请求成功的回掉函数
//treeId-mytree
function zTreeOnAsyncSuccess(event, treeId, treeNode, msg) {
	var userId=treeNode.userId;
	var treeObj = $.fn.zTree.getZTreeObj("deptTree");
	console.log("------------parentZNode--------",treeNode);//当前节点
	if(!treeNode.children||!treeNode.children.length){
		if(treeNode.bloodType==0){
			//加载部门树
		    Ajax.request({
		        url: "../user/getUserRecommondUsers?userId="+userId,
		        async: true,
		        successCallback: function (r) {
		            treeObj.addNodes(treeNode,r.bonusPointsVos, false);
		        }
		    });
		}else if(treeNode.bloodType==1){
			//加载部门树
		    Ajax.request({
		        url: "../user/getUserNodeUsers?userId="+userId,
		        async: true,
		        successCallback: function (r) {
		            treeObj.addNodes(treeNode,r.bonusPointsVos, false);
		        }
		    });
		}
		
	}
	
};

//异步请求失败的回调函数
function zTreeOnAsyncError(event, treeId, treeNode, XMLHttpRequest, textStatus, errorThrown) {
    alert('请求失败！');
//    alert(XMLHttpRequest);
};

var IDMark_Switch = "_switch",
IDMark_Icon = "_ico",
IDMark_Span = "_span",
IDMark_Input = "_input",
IDMark_Check = "_check",
IDMark_Edit = "_edit",
IDMark_Remove = "_remove",
IDMark_Ul = "_ul",
IDMark_A = "_a";
function addDiyDom(treeId, treeNode) {
	console.log("----addDiyDom--------",treeNode);
	var aObj = $("#" + treeNode.tId + IDMark_A);
	if(treeNode.bloodType==0)return;
	if(treeNode.bloodType==1){
		var diyView='<span>-----</span>';
		if(treeNode.invitedRightUserId&&treeNode.invitedRightUserId>0){
			diyView=diyView+"(右节点)"+treeNode.invitedRightUserId+",团队业绩："+treeNode.bonusTeamInvitedPoints+",个人业绩："+treeNode.bonusMeInvitedPoints;;
		}else if(treeNode.invitedUserId&&treeNode.invitedUserId>0){
			diyView=diyView+"(左节点)"+treeNode.invitedUserId+",团队业绩："+treeNode.bonusTeamInvitedPoints+",个人业绩："+treeNode.bonusMeInvitedPoints;
		} 
		aObj.after(diyView);
		return ;
	}
}

var setting = {
		view: {
			addDiyDom: addDiyDom
		},
		async:{  
	        enable: true,
            dataType: "json",
            url: "",
            autoParam: ["userId"],
            type:'post',
	    },  
	    data: {
	        simpleData: {
	            enable: true,
	            idKey: "userId",
	            pIdKey: "invitedUserId",
	            rootPId: -1
	        },
	        key: {
	            url: "nourl",
	            name:"userName",
	        }
	    },
        callback : {  
            beforeAsync : zTreeBeforeAsync,  
            onAsyncSuccess : zTreeOnAsyncSuccess,  
            onClick:zTreeOnAsyncSuccess  
        }  
};



var ztree;


let vm = new Vue({
	el: '#rrapp',
	data: {
        showList: true,
        title: null,
		user: {},
		ruleValidate: {
			userName: [
				{required: true, message: '登录账号不能为空', trigger: 'blur'}
			],
		},
		q: {
		    userName: '',
		    mobile:'',
		    state:''
		},
		userStatuss:[],
		userSexs:[],
		userRoleTypes:[],
		userNamedTypes:[],
		addIntegerUser:{
			userId:'',
			userName:'',
			integralScore:1
		},
		provinceId:31068,
        cityId:31681,
        regionId:31829,
        areaId:31850,
        regions: [],
        provinceItems:[],
        cityItems:[],
        regionItems:[],
        areaItems:[],
        signupUserLevelTypes:[],
        
        auditUserSuccessModal:false,
        auditUserId:0,
        signupUser:{},
        invitedUser:{},
        inviteNodeUser:{},
        subNodeUsers:[],
        
        rechargeMoneyModal:false,
        money:0,
        modShareLastTimeModal:false,
        
        userCenterShowModal:false,
        fwManagerEntity:{},
        bonusPointsVo: {
        	userName: "根节点",
        	invitedUserId: 0,
            orderNum: 0,
            userId: 0,
        },
        helpRegistUser:{},
        
        modUserLevelModal:false,
        
        
        showUserInfoModal:false,
        showNodeModal:false,
        
        recommondBonusPointsVo: {
        },
        nodeBonusPointsVo: {
        },
        recommondUserVo: {
        },
        nodeUserVo: {
        },
        curUserId:'',
        
        showMoveMoneyModal:false,
        moveMoney:'',
        movePayUserId:'',
        moveToUserId:'',
        
	},
	methods: {
		query: function () {
			vm.reload();
		},
		
		add: function () {
			vm.showList = false;
//			vm.ruleValidate.password[0].required=true;
//            vm.ruleValidate.password2[0].required=true;
			vm.title = "新增";
			vm.user = {
					createUserId:0
			};
		},
		update: function (event) {
            let userId = getSelectedRow("#jqGrid");
			if (userId == null) {
				return;
			}
			vm.showList = false;
//			vm.ruleValidate.password[0].required=false;
//            vm.ruleValidate.password2[0].required=false;
            vm.title = "修改";

            vm.getInfo(userId)
		},
		saveOrUpdate: function (event) {
			
		 console.log("----saveOrUpdate--------");
			
            let url = vm.user.userId == null ? "../user/save" : "../user/update";
//            if(vm.user.userId){
//            	if(vm.user.password||vm.user.password2){
//            		if(vm.user.password!=vm.user.password2){
//            			layer.alert("密码和确认密码不一致！");
//            			return;
//            		}
//            	}
//            }else{
//            	if(vm.user.password!=vm.user.password2){
//            		layer.alert("密码和确认密码不一致！");
//            		return;
//            	}
//            }
            
            Ajax.request({
			    url: url,
                params: JSON.stringify(vm.user),
                type: "POST",
			    contentType: "application/json",
                successCallback: function (r) {
                    alert('操作成功', function (index) {
                        vm.reload();
                    });
                }
			});
		},
		toBonusTask: function (event) {
			confirm('确定立即支持分红任务？', function () {
                Ajax.request({
				    url: "../user/toRunBonusTask",
                    params: {},
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
		
		del: function (event) {
            let userIds = getSelectedRows("#jqGrid");
			if (userIds == null){
				return;
			}

			confirm('确定要删除选中的记录？', function () {
                Ajax.request({
				    url: "../user/delete",
                    params: JSON.stringify(userIds),
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
		toStopBonusTask: function (event) {
			let userIds = getSelectedRows("#jqGrid");
			if (userIds == null){
				return;
			}
			confirm('确定要停止选中用户分红？', function () {
				Ajax.request({
					url: "../user/stopUserState",
					params: JSON.stringify(userIds),
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
		toBrokeNode: function (event) {
			 let userId = getSelectedRow("#jqGrid");
				if (userId == null) {
					return;
			 }
			confirm('确定断掉该用户的节点关系？', function () {
				Ajax.request({
					url: "../user/toBrokeNode",
					params: {
						userId:userId
					},
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
		getInfo: function(userId){
            Ajax.request({
                url: "../user/info/"+userId,
                async: true,
                successCallback: function (r) {
                    vm.user = r.user;
                    if(vm.user){
                    	vm.user.state=vm.user.state+'';
                    	Ajax.request({
                            url: "../user/info/"+vm.user.createUserId,
                            async: true,
                            successCallback: function (r) {
                            	 vm.signupUser = r.user;
                            }
                        });
                    	
                    }
                    
                }
            });
		},
		
		reload: function (event) {
			vm.showList = true;
            let page = $("#jqGrid").jqGrid('getGridParam', 'page');
//            var querParam={
//            };
//            if(vm.q.state&&vm.q.state!=""){
//            	querParam.state=vm.q.state;
//            }
//            if(vm.q.state&&vm.q.state!=""){
//            	querParam.state=vm.q.state;
//            }
//            
//            if(vm.q.userName&&vm.q.userName!=""){
//            	querParam.userName=vm.q.userName;
//            }
//            if(vm.q.mobile&&vm.q.mobile!=""){
//            	querParam.mobile=vm.q.mobile;
//            }
            
			$("#jqGrid").jqGrid('setGridParam', {
                postData: {
        		    userName: vm.q.userName,
        		    mobile:vm.q.mobile,
        		    state:vm.q.state,
        		    aOrB:vm.q.aOrB
        		},
                page: page
            }).trigger("reloadGrid");
            vm.handleReset('formValidate');
		},
        reloadSearch: function() {
            vm.q = {
        		    userName: '',
        		    mobile:'',
        		    state:''
        		}
            vm.addIntegerUser={
            		userId:'',
        			userName:'',
        			integralScore:1
            }
            vm.reload();
        },
        handleSubmit: function (name) {
        	console.log("---handleSubmit--",name);
//            handleSubmitValidate(this, name, function () {
//                 vm.saveOrUpdate();
//            });
            vm.saveOrUpdate();
        },
        handleReset: function (name) {
            handleResetForm(this, name);
        },
        
        
        selectMember: function () {
            openWindow({
                type: 2,
                title: '选择关联用户',
                area: ['1030px', '500px'],
                content: ['../shop/user.html'],
                btn: ['确定', '取消'],
                btn1: function (index,layero) {
                	var artFrameWd=layero.find("iframe")[0].contentWindow;
                    var grid = artFrameWd.$("#jqGrid");
                    var rowKey = grid.getGridParam("selrow");
                    if (!rowKey) {
                        iview.Message.error("请选择一条记录");
                        return;
                    }
                    var selectedIDs = grid.getGridParam("selarrrow");
                    if (selectedIDs.length > 1) {
                        iview.Message.error("只能选择一条记录");
                        return;
                    }
                    var memberId=selectedIDs[0];
                    console.log("---memberId---", memberId);
                    //设置新的框Id
                    vm.user.createUserId = memberId;
                    top.layer.close(index);
                    layer.close(index);
            }
            });
        },
        changeProvince:function (opt) {
        	console.log("--------opt--------",opt);
            vm.cityItems=[];
            vm.areaItems=[];
        	vm.regionItems=[];
            var id=opt;
            Ajax.request({
                url: "../sys/region/getSubAreaByParentId?parentId=" + id,
                async: true,
                successCallback: function (r) {
                    vm.cityItems = r.list;
                }
            });
        },
        
        changeCity:function (opt) {
        	console.log("--------opt--------",opt);
        	vm.areaItems=[];
        	vm.regionItems=[];
            var id=opt;
            Ajax.request({
                url: "../sys/region/getSubAreaByParentId?parentId=" + id,
                async: true,
                successCallback: function (r) {
                    vm.regionItems = r.list;
                }
            });
        },
        changeRegion:function (opt) {
        	console.log("--------opt--------",opt);
        	var value=opt.value;
        	var id=opt;
        	Ajax.request({
        		url: "../sys/region/getSubAreaByParentId?parentId=" + id,
        		async: true,
        		successCallback: function (r) {
        			vm.areaItems = r.list;
        		}
        	});
        },
        /**
         * 客户状态
         */
        getUserStatuss: function () {
            Ajax.request({
                url: "../sys/macro/queryMacrosByValue?value=userStatus",
                async: false,
                successCallback: function (r) {
                    vm.userStatuss = r.list;
                }
            });
        },
        /**
         * 客户性别
         */
        getUserSexs: function () {
            Ajax.request({
                url: "../sys/macro/queryMacrosByValue?value=userSex",
                async: false,
                successCallback: function (r) {
                    vm.userSexs = r.list;
                }
            });
        },
        /**
         * 分销级别
         */
        getUserRoleTypes: function () {
            Ajax.request({
                url: "../sys/macro/queryMacrosByValue?value=userRoleType",
                async: false,
                successCallback: function (r) {
                    vm.userRoleTypes = r.list;
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
        /**
         *级别
         */
        getSignupUserLevelTypes: function () {
            let that=this;
            Ajax.request({
            	url: "../userinvestlevel/queryAll",
            	async: false,
            	successCallback: function (r) {
            		that.signupUserLevelTypes = r.list;
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
        toIntegralList:function(){
        	menuItem("shop/integralcount.html",-2,"转积分记录");
        },
        
        getUserAllInfo: function(userId){
        	
            Ajax.request({
                url: "../user/info/"+userId,
                async: false,
                successCallback: function (r) {
                	 vm.user = r.user;
                }
            });
            console.log("--------user-----",vm.user);
            var createUserId=vm.user.createUserId;
            var signupInvitedPhone=vm.user.signupInvitedPhone;
            var signupNodePhone=vm.user.signupNodePhone;
            
            Ajax.request({
                url: "../user/info/"+createUserId,
                async: true,
                successCallback: function (r) {
                	 vm.signupUser = r.user;
                }
            });
            
            Ajax.request({
                url: "../user/getUserNode?nodeUserName="+signupInvitedPhone,
                async: true,
                successCallback: function (r) {
                	 vm.invitedUser = r.nodeUserEntity;
                }
            });
            Ajax.request({
            	url: "../user/getUserNode?nodeUserName="+signupNodePhone,
            	async: true,
            	successCallback: function (r) {
            		vm.inviteNodeUser = r.nodeUserEntity;
            	}
            });
            
            Ajax.request({
            	url: "../user/getUserBinaryTreeSubNode?nodeUserName="+signupNodePhone,
            	async: true,
            	successCallback: function (r) {
            		vm.subNodeUsers = r.bonusPointsEntities;
            	}
            });
            
		},
        
        auditUserSuccessModalTap:function(){
        	vm.auditUserSuccessModal=true;
        },
        
        auditUserSuccess:function(){
        	var userId=vm.auditUserId;
        	Ajax.request({
        		url: '../user/confirmSignUp',
        		params: {
        			userId:userId
        		},
        		contentType: "application/json",
        		successCallback: function (r) {
        			vm.auditUserSuccessModal=false;
        			alert('操作成功', function (index) {
        				vm.reload();
        			});
        		}
        	});
        	
        },
        toConfirmSignUp: function(userId,userName){
        	console.log(userId,userName)
        	vm.auditUserId=userId;
        	vm.auditUserSuccessModal=true;
        	vm.getUserAllInfo(userId);
//        	vm.addIntegerUser.userId=userId;
//        	vm.addIntegerUser.userName=userName;
//        	vm.addIntegerUser.integralScore=1;
//        	console.log(vm.addIntegerUser)
//        	int userId=user.getUserId();//注册的新用户
//    	int signupUserId=user.getCreateUserId();//代注册的用户；
//    	String nodeUserName=user.getInvitedNodePhone();//节点账号
//    	String recommondUserName=user.getInvitedPhone();//推荐人账号
        	
//        	vm.auditUserSuccessModal=true;
        },
        
        showInfo:function(userId){
        	vm.showList = false;
        	vm.showUserInfoModal = true;
        	vm.showNodeModal = false;
            vm.title = "查看";
            vm.getInfo(userId)
        	
        },
        toOpenRechargeMoneyModal:function(userId){
        	vm.auditUserId=userId;
        	vm.money=0;
        	vm.rechargeMoneyModal=true;
        	this.getInfo(userId);
        },
        toRechargeMoneyTap:function(){
        	var userId=vm.auditUserId;
        	var money=vm.money;
        	Ajax.request({
			    url: '../user/toRecharge',
                params: {
                	userId:userId,
                	money:money,
                 },
			    contentType: "application/json",
                successCallback: function (r) {
                	vm.rechargeMoneyModal=false;
                    alert('操作成功', function (index) {
                        vm.reload();
                    });
                }
			});
        },
        toModShareLastTimeModal: function (event) {
            let userId = getSelectedRow("#jqGrid");
			if (userId == null) {
				return;
			}
			vm.modShareLastTimeModal = true;
            vm.getInfo(userId)
		},
		
        modShareLastTimeTap:function(){
        	var userId=vm.user.userId;
        	var shareInvestLastTime=vm.user.shareInvestLastTime;
        	Ajax.request({
			    url: '../user/updateLastBonusTime',
                params: {
                	userId:userId,
                	shareInvestLastTime:shareInvestLastTime,
                 },
			    contentType: "application/json",
                successCallback: function (r) {
                	vm.modShareLastTimeModal=false;
                    alert('操作成功', function (index) {
                        vm.reload();
                    });
                }
			});
        },
        
        showRecommondTree: function (userId) {
			 Ajax.request({
	                url: "../user/info/"+userId,
	                async: false,
	                successCallback: function (r) {
	                	 vm.user = r.user;
	               }
	          });
			vm.bonusPointsVo.userId=userId;
			vm.bonusPointsVo.userName = vm.user.userName;
        	//加载部门树
            Ajax.request({
                url: "../user/getUserRecommondUsers?userId="+userId,
                async: true,
                successCallback: function (r) {
                	 console.log("------r---------",r);
                	 r.bonusPointsVos.unshift(vm.bonusPointsVo);
                    ztree = $.fn.zTree.init($("#deptTree"), setting, r.bonusPointsVos);
                    var node = ztree.getNodeByParam("userId", userId);
                    ztree.selectNode(node);
                    
                }
            });
            openWindow({
                title: "查看推荐树",
                area: ['800px', '450px'],
                content: jQuery("#deptLayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    layer.close(index);
                }
            });
        },
        
        showUserStat: function (userId) {
            openWindow({
            	top:true,
                type: 2,
                area: ['95%', '95%'],
                title: '<i class="fa fa-print"></i>会员对账',
                content: 'shop/userstat.html?userId=' + userId,
                btn: ['确定'],
            })
        },
        
        showNodeTree: function (userId) {
			 Ajax.request({
	                url: "../user/info/"+userId,
	                async: false,
	                successCallback: function (r) {
	                	 vm.user = r.user;
	               }
	          });
			vm.bonusPointsVo.userId=userId;
			vm.bonusPointsVo.userName = vm.user.userName;
       	//加载部门树
           Ajax.request({
               url: "../user/getUserNodeUsers?userId="+userId,
               async: true,
               successCallback: function (r) {
               	 console.log("------r---------",r);
               	 r.bonusPointsVos.unshift(vm.bonusPointsVo);
                   ztree = $.fn.zTree.init($("#deptTree"), setting, r.bonusPointsVos);
                   var node = ztree.getNodeByParam("userId", userId);
                   ztree.selectNode(node);
               }
           });
           openWindow({
               title: "查看节点树",
               area: ['800px', '450px'],
               content: jQuery("#deptLayer"),
               btn: ['确定', '取消'],
               btn1: function (index) {
                   layer.close(index);
               }
           });
       },
       
       toModUserLevelModal:function(){
    	let userId = getSelectedRow("#jqGrid");
		if (userId == null) {
			return;
		}   
    	vm.modUserLevelModal=true;
       	this.getInfo(userId);
       },
       
       toModUserLevelTap:function(){
       	Ajax.request({
			    url: '../user/toUpdateUserLevel',
                params: {
               	userId:vm.user.userId,
               	userLevelType:vm.user.userLevelType,
                },
			   contentType: "application/json",
               successCallback: function (r) {
               	vm.rechargeMoneyModal=false;
                   alert('操作成功', function (index) {
                       vm.reload();
                   });
               }
			});
       },
       
       
       showModNode:function(userId){
			vm.showList = false;
			vm.showUserInfoModal = false;
			vm.showNodeModal = true;
			vm.title = "更新节点";
			vm.curUserId=userId;
			 vm.recommondUserVo={};
			 vm.nodeUserVo={};
			 Ajax.request({
	                url: "../user/getUserParentNode?userId="+userId,
	                async: true,
	                successCallback: function (r) {
	                	 console.log("------r---------",r);
	                	 var meBvo=r.bonusPointsVo;
	                	 var parentId=meBvo.invitedUserId;
	                	 if(parentId==null||parentId==0){
	                		 parentId=meBvo.invitedRightUserId;
	                	 }
	                	 Ajax.request({
	     	                url: "../user/getUserParentNode?userId="+parentId,
	     	                async: true,
	     	                successCallback: function (r) {
	     	                	 console.log("------r---------",r);
	     	                	 vm.nodeBonusPointsVo=r.bonusPointsVo;
	     	                }
	     	            });
	                }
	        });
			 
			 Ajax.request({
				 url: "../user/getUserParentRecommond?userId="+userId,
				 async: true,
				 successCallback: function (r) {
					 console.log("------r---------",r);
					 var meBvo=r.bonusPointsVo;
                	 var parentId=meBvo.invitedUserId;
                	 Ajax.request({
     	                url: "../user/getUserParentNode?userId="+parentId,
     	                async: true,
     	                successCallback: function (r) {
     	                	 console.log("------r---------",r);
     	                	 vm.recommondBonusPointsVo=r.bonusPointsVo;
     	                }
     	            });
                	 
				 }
			 });
		},
       selectRecommondUser: function () {
           openWindow({
               top:true,
               type: 2,
               area: ['95%', '95%'],
               title: '选择用户',
               content: ['shop/user.html'],
               btn: ['确定', '取消'],
               btn1: function (index,layero) {
               	var artFrameWd=layero.find("iframe")[0].contentWindow;
                   var grid = artFrameWd.$("#jqGrid");
                   var rowKey = grid.getGridParam("selrow");
                   if (!rowKey) {
                       iview.Message.error("请选择一条记录");
                       return;
                   }
                   var selectedIDs = grid.getGridParam("selarrrow");
                   if (selectedIDs.length > 1) {
                       iview.Message.error("只能选择一条记录");
                       return;
                   }
                   var id=selectedIDs[0];
                   var userInfo=grid.jqGrid('getRowData', id);
                   console.log("---userInfo---", userInfo);
                   vm.recommondUserVo=userInfo;
                   top.layer.close(index);
                   layer.close(index);
             }
           });
       },
       selectNodeUser: function () {
           openWindow({
               top:true,
               type: 2,
               area: ['95%', '95%'],
               title: '选择用户',
               content: ['shop/user.html'],
               btn: ['确定', '取消'],
               btn1: function (index,layero) {
               	var artFrameWd=layero.find("iframe")[0].contentWindow;
                   var grid = artFrameWd.$("#jqGrid");
                   var rowKey = grid.getGridParam("selrow");
                   if (!rowKey) {
                       iview.Message.error("请选择一条记录");
                       return;
                   }
                   var selectedIDs = grid.getGridParam("selarrrow");
                   if (selectedIDs.length > 1) {
                       iview.Message.error("只能选择一条记录");
                       return;
                   }
                   var id=selectedIDs[0];
                   var userInfo=grid.jqGrid('getRowData', id);
                   console.log("---userInfo---", userInfo);
                   vm.nodeUserVo=userInfo;
                   top.layer.close(index);
                   layer.close(index);
             }
           });
       },
       changeNodeTap:function(){
    	   let that=this;
    	   var userId=that.curUserId;
    	   var parentNodeUserId=vm.nodeUserVo.userId;
    	   var parentInvitedUserId=vm.recommondUserVo.userId;
    	   var url="../user/toUpdateUserNode?userId="+userId+"&parentNodeUserId="+parentNodeUserId+"&parentInvitedUserId="+parentInvitedUserId;
    	   console.log("--------param--------",url);
    	   Ajax.request({
				 url: url,
				 async: true,
				 successCallback: function (r) {
					 console.log("------r---------",r);
					 if(r.errno==0){
						 alert('操作成功', function (index) {
		                       vm.reload();
		                   }); 
					 }
				 }
		   });
       },
       
       toShowMoveMoneyTap:function(){
    	   vm.showMoveMoneyModal=true;
       },
       toMoveMoneyTap:function(){
    	    var userId=vm.movePayUserId;
    	    var moveToUserId=vm.moveToUserId;
	       	var money=vm.moveMoney;
	       	Ajax.request({
				    url: '../user/toMoveMoney',
	               params: {
	               	userId:userId,
	               	toUserId:moveToUserId,
	               	money:money,
	                },
//				   contentType: "application/json",
	               successCallback: function (r) {
	               	vm.rechargeMoneyModal=false;
	                   alert('操作成功', function (index) {
	                       vm.reload();
	                   });
	               }
			});
       },
       exportUsers:function(){
          var postData={
       		   userName: vm.q.userName,
      		    mobile:vm.q.mobile
                	};
       	exportFile('#rrapp', '../user/exportUsers',postData);
       },
       
       showUserCenter:function(userId){
    	   vm.fwManagerEntity={};
    	   Ajax.request({
			    url: '../user/tofindUserCenter',
              params: {
              	userId:userId,
               },
              successCallback: function (r) {
              	vm.userCenterShowModal=true;
                vm.fwManagerEntity=r.fwManagerEntity;
              }
		});
      }
	},
    created: function () {
        let that = this;
        console.log("--------provinceId--------",that.provinceId);
        Ajax.request({
            url: "../sys/region/getSubAreaByParentId?parentId=" + 1,
            async: true,
            successCallback: function (r) {
                that.provinceItems = r.list;
                if(r.list.length>0){
                  that.changeProvince(r.list[0].id);
                }
//                that.changeProvince(that.provinceId);
//                that.changeCity(that.cityId);
//                that.changeRegion(that.regionId);
            }
        });
        that.getSignupUserLevelTypes();
        
    }
});

