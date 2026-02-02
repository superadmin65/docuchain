var adminUsers = angular.module('dapp.AdminUsersController', ['ui.select', 'ngSanitize', 'angularUtils.directives.dirPagination']);

adminUsers.controller('AdminUsersController', ['$scope', '$window', '$location', '$state', '$rootScope', 'toaster', '$http', '$timeout', 'FunctionalityService', function ($scope, $window, $location, $state, $rootScope, toaster, $http, $timeout, FunctionalityService, Upload) {

  $scope.userProfileId = $window.localStorage.getItem('userId');
  $scope.organizationId = $window.localStorage.getItem('organizationId');
  $scope.maxUserCount = $window.localStorage.getItem('maxUserCount');

  $scope.adminUserList = [];
  $scope.selctedUserList = [];
  $scope.example1model = [];
  $scope.example2settings = { displayProp: 'id' };
  $scope.popuptitle = "Add New User";
  $scope.myFile = "";
  $scope.file;
  $scope.user = {};
  $scope.password = true;
  $scope.confirmPassword = true;
  $scope.roleCheckmultiselece = true;
  $scope.roleCheck = false;
  $scope.updateUNameEnable = false;
  $scope.updateUNameDisable = true;
  $scope.currentPage = 1;
  $scope.viewby = 10;
  $scope.uiselectshow = true;
  $scope.signleVesselName = false;
  $scope.itemsPerPage = $scope.viewby;
  $scope.addPopupsubmit = true;
  $scope.editPopupsubmit = false;
  $scope.isReadOnly = true;
  $scope.loader = false;
  var buttonVessel = document.getElementById("addUserlimits");

  //  $scope.roleList = {
  //   option1: "Super Admin",
  //   option2: "Admin",
  //   option3: "ShipMaster",
  //   option4: "Commercial Manager",
  //   option5: "DateOpertor"
  // }

  // $scope.users = [{
  //   "id": 1,
  //   "username": "John"
  // }, {
  //   "id": 2,
  //   "username": "Mike"
  // }, {
  //   "id": 3,
  //   "username": "Dean"
  // }, {
  //   "id": 4,
  //   "username": "Peter"
  // }];
  $scope.$on('$viewContentLoaded', function () {
    FunctionalityService.getVesselsNameList($scope.userProfileId)
      .then(function (response) {
        if (response.status == 200) {
          $scope.users = response.data.shipProfileList;
        }
      }, function (error) {
        console.log("message :: " + error);
      });
  })

  $scope.$on('$viewContentLoaded', function () {
    FunctionalityService.getOrganizationUserList($scope.userProfileId)
      .then(function mySuccess(response) {
        if (response.status == 201 || response.status == 200) {
          $scope.adminUserList = JSON.stringify(response.data.getUserList);
          $scope.adminUserList = response.data.getUserList;
          if ($scope.adminUserList == undefined) {
            // toaster.clear();
            // toaster.info({ title: "No records found" });
          }
        }
      }, function (error) {
        $scope.status = 'Unable to Create Room: ' + error;
      });
  })

  $scope.$on('$viewContentLoaded', function () {
    FunctionalityService.getRoleList($scope.userProfileId)
      .then(function (response) {
        if (response.status == 200) {
          $scope.roleList = response.data.roleAliasInfos;
        }
      }, function (error) {
        console.log("message :: " + error);
      });
  })
  $scope.checkUserlimit = function () {
    if ($scope.adminUserList.length >= $scope.maxUserCount) {
            toaster.error("User limit exceedeed")
            buttonVessel.disabled = true;
    }
    else {
      buttonVessel.disabled = false;
      $scope.addUserModel();
    }
  }
  $scope.selectedUsers = [];

  $scope.thumbnail = {
    // dataUrl: 'adsfas'
  };

  $scope.fileReaderSupported = window.FileReader != null;
  $scope.uploadFile = function (files) {
    if (files != null) {
      $scope.file = files[0];
      if ($scope.fileReaderSupported && $scope.file.type.indexOf('image') > -1) {
        $timeout(function () {
          var fileReader = new FileReader();
          fileReader.readAsDataURL($scope.file);
          fileReader.onload = function (e) {
            $timeout(function () {
              $scope.thumbnail.dataUrl = e.target.result;
            });
          }
        });
      }
    }
  };
  $scope.uploadFileedit = function (files) {
    if (files != null) {
      $scope.file = files[0];
      if ($scope.fileReaderSupported && $scope.file.type.indexOf('image') > -1) {
        $timeout(function () {
          var fileReader = new FileReader();
          fileReader.readAsDataURL($scope.file);
          fileReader.onload = function (e) {
            $timeout(function () {
              $scope.thumbnail.dataUrledit = e.target.result;
            });
          }
        });
      }
    }
  };

  $scope.addUser = function (user) {
    $scope.loader = true;

    if (user.password !== user.confirmPassword) {
      $scope.loader = false;

      toaster.error("error", "Password does not match")
    } else {


      if (user.id == undefined) {
        var userInfoId = [];
        var appendId = [];
        //$scope.selectedUserslist = user.selectedUsers;
        //console.info(" $scope.selectedUserslist", user.selectedUserslist);
        if (user.selectedCategory.roleId == 3 && user.selectedUsers != null) {
          userInfoId.push(user.selectedUsers.id);
        } else {
          angular.forEach(user.selectedUsers, function (infos) {
            userInfoId.push(infos.id);            
            appendId.push(infos.id);
            appendId.push('@');
          });
        }
        var userInfo = {
          "firstName": user.firstName,
          "lastName": user.lastName,
          "userName": user.userName,
          "password": user.password,
          "roleId": user.selectedCategory.roleId,
          "businessCategory": user.selectedCategory.roleAliasName,
          "mail": user.mail,
          "organizationId": $scope.organizationId,
          "loginId": $scope.userProfileId,
          "shipProfileIds": userInfoId
        }
        FunctionalityService.addUser(userInfo, $scope.file).then(function (response) {
          $scope.loader = false;

          if (response.status == 200 || response.status == 201) {
            $('#addNewUser').modal('hide');
            $timeout(function () {
              toaster.success({ title: response.data.message });
              // toaster.pop('success', response.data.message); 
            }, 300);
            $state.reload();
          } else {
            toaster.clear();
            toaster.error({ title: response.data.message });
          }
        }, function myError(err) {
          $scope.loader = false;
          console.log("Error response", err);
        });
      } else if (user.id != undefined) {
        $scope.userInfoIdupdateEdit = [];
        //$scope.selectedUserslist = user.selectedUsersEdit;
        if (user.roleId == 3) {
          if (user.selectedUsersEdit != null)
            $scope.userInfoIdupdateEdit.push(user.selectedUsersEdit.id);
        } else {
          angular.forEach(user.selectedUsersEdit, function (infos) {
            if (infos.id != null)
              $scope.userInfoIdupdateEdit.push(infos.id);
          });
        }

        var userInfo = {
          "firstName": user.firstNameEdit,
          "lastName": user.lastNameEdit,
          "mail": user.mailEdit,
          "userId": user.id,
          "shipProfileIds": $scope.userInfoIdupdateEdit
        }
        FunctionalityService.editUser(userInfo, $scope.file).then(function mySuccess(response) {
          $scope.loader = false;

          if (response.status == 200 || response.status == 201) {
            $('#ediUserinfo').modal('hide');
            toaster.pop('success', response.data.message);
            setTimeout(function () {
              $state.reload();
            }, 1000);
          }
          else {
            toaster.pop('error', response.data.message);
          }

        }, function myError(err) {
          $scope.loader = false;
          console.log("Error response", err);
        });
      }
    }
  }


  $scope.deleteuserId = [];
  $scope.getDeleteUserId = function (id) {
    $scope.deleteuserId.push(id);

  }

  $scope.deleteUser = function () {
    $scope.loader = true;
    var deleteUser = { "userIds": $scope.deleteuserId, "loginId": $scope.userProfileId }
    FunctionalityService.deleteAdminUser(deleteUser)
      .then(function (response) {
        $scope.loader = false;
        if (response.status == 200) {
          $('#delete').modal('hide');
          toaster.pop('success', response.data.message);
          setTimeout(function () {
            $state.reload();
          }, 1000);
        }
        else {
          toaster.error({ title: response.data.message });
        }

      }, function myError(err) {
        $scope.loader = false;
        console.log("Error response", err);
      });
  }

  $scope.changeStatusAdmin = function (status) {
    $scope.loader = true;

    var data = status.userId;
    FunctionalityService.getchangeStatusOfUser(data)
      .then(function mySuccess(response) {
        $scope.loader = false;

      }, function myError(err) {
        $scope.loader = false;
        console.log("Error response", err);
      });
  }

  $scope.checkUncheckAll = function () {
    $scope.selctedUserList = [];
    if ($scope.checkall) {
      $scope.checkall = true;
      angular.forEach($scope.adminUserList, function (value) {
        $scope.selctedUserList.push(value.userId);
      });
    } else {
      $scope.checkall = false;
    }
    angular.forEach($scope.adminUserList, function (user) {
      user.checked = $scope.checkall;
    });
  };


  $scope.updateCheckall = function ($index, user) {
    var userTotal = $scope.adminUserList.length;
    var count = 0;
    $scope.selctedUserList = [];
    angular.forEach($scope.adminUserList, function (item) {
      if (item.checked) {
        count++;
        $scope.selctedUserList.push(item.userId);
      }
    });
    if (userTotal == count) {
      $scope.checkall = true;
    } else {
      $scope.checkall = false;
    }
  };

  $scope.singleToggle = function (object) {
    $scope.loader = true;

    toaster.clear();   
    $scope.selctedUserList = [];
    $scope.selctedUserList.push(object.userId);


    if (object.status == 1) {
      var data = {
        "userIds": $scope.selctedUserList,
        "loginId": $scope.userProfileId,
        "status": 0
      }
      FunctionalityService.deActivateAllUser(JSON.stringify(data)).then(function (response) {
        $scope.loader = false;
        if (response.status == 201 || response.status == 200) {

          toaster.pop('success', response.data.message);
          setTimeout(function () {
            $state.reload();
          }, 1000);
          $scope.selctedUserList = [];
          $scope.checkall = false;

        }
        else if (response.status == 206) {
          toaster.pop('error', response.data.message);
        }
      }, function myError(err) {
        $scope.loader = false;
        console.log("Error response", err);
      });

    }
    else {
      var data = {
        "userIds": $scope.selctedUserList,
        "loginId": $scope.userProfileId,
        "status": 1
      }
      FunctionalityService.activateAllUser(JSON.stringify(data)).then(function (response) {
        $scope.loader = false;

        if (response.status == 201 || response.status == 200) {
          toaster.pop('success', response.data.message);
          setTimeout(function () {
            $state.reload();
          }, 1000);
          $scope.selctedUserList = [];
          $scope.checkall = false;
        }
        else if (response.status == 206) {
          toaster.pop('error', response.data.message);
        }
      }, function myError(err) {
        $scope.loader = false;
        console.log("Error response", err);
      });
    }
  };

  $scope.toggleChange = function (user) {
    $scope.loader = true;
    $scope.selctedUserList.push(user.userId);
    if (user.status == 1) {
      var userData = { "userIds": $scope.selctedUserList, "status": 0, "loginId": $scope.userProfileId }
      FunctionalityService.activateAllUser(JSON.stringify(userData))
        .then(function (response) {
          $scope.loader = false;

          if (response.status == 200) {
            toaster.pop('success','User deactivated successfully');
            setTimeout(function () {
              $state.reload();
            }, 1000);;
          }
          else {
            toaster.pop('error', response.data.message);
          }

        }, function myError(err) {
          $scope.loader = false;
          console.log("Error response", err);
        });
    } else {
      var userData = { "userIds": $scope.selctedUserList, "status": 1, "loginId": $scope.userProfileId }
      FunctionalityService.deActivateAllUser(JSON.stringify(userData))
        .then(function (response) {
          $scope.loader = false;

          if (response.status == 200) {
            toaster.pop('success', 'User activated successfully');
            setTimeout(function () {
              $state.reload();
            }, 1000);;
          }
          else {
            toaster.pop('error', response.data.message);

          }

        }, function myError(err) {
          $scope.loader = false;
          console.log("Error response", err);
        });
    }
  }

  $scope.activateAllUser = function () {
    $scope.loader = true;

    var userData = { "userIds": $scope.selctedUserList, "status": 1, "loginId": $scope.userProfileId }
    FunctionalityService.activateAllUser(JSON.stringify(userData))
      .then(function (response) {
        $scope.loader = false;

        if (response.status == 200) {
          toaster.pop('success', response.data.message);
          setTimeout(function () {
            $state.reload();
          }, 1000);;

        }
        else {
          toaster.pop('error', response.data.message);

        }

      }, function myError(err) {
        $scope.loader = false;
        console.log("Error response", err);
      });
  }

  $scope.deActivateAllUser = function () {
    $scope.loader = true;

    if ($scope.selctedUserList.length == 0) {

    }
    var userData = { "userIds": $scope.selctedUserList, "status": 0, "loginId": $scope.userProfileId }
    FunctionalityService.deActivateAllUser(JSON.stringify(userData))
      .then(function (response) {
        $scope.loader = false;

        if (response.status == 200) {
          toaster.pop('success', response.data.message);
          setTimeout(function () {
            $state.reload();
          }, 1000);;

        }
        else {
          toaster.pop('error', response.data.message);

        }

      }, function myError(err) {
        $scope.loader = false;
        console.log("Error response", err);
      });
  }
  $scope.deleteAllUser = function () {
    $scope.loader = true;

    var userData = { "userIds": $scope.selctedUserList, "loginId": $scope.userProfileId }
    FunctionalityService.deleteAdminUser(JSON.stringify(userData))
      .then(function (response) {
        $scope.loader = false;

        if (response.status == 200) {
          toaster.pop('success', response.data.message);
          setTimeout(function () {
            $state.reload();
          }, 1000);;

        }
        else {
          toaster.pop('error', response.data.message);

        }

      }, function myError(err) {
        $scope.loader = false;
        console.log("Error response", err);
      });
  }
  adminUsers.directive("fileInput", ['$parse', function ($parse) {
    return {
      restrict: 'A',
      link: function (scope, ele, attrs) {
        ele.bind('change', function () {
          $parse(attrs.fileInput).
            assign(scope, ele[0].files)
          scope.$apply()
        });
      }
    }
  }]);

  // $rootScope.user.lastName='';
  $scope.editUser = function (userId) {
    $scope.popuptitle = "Edit User";
    $scope.selectedUsers = [];
    $scope.roleCheckmultiselece = false;
    $scope.roleCheck = true;
    $scope.password = false;
    $scope.confirmPassword = false;
    $scope.updateUNameDisable = false;
    $scope.updateUNameEnable = true;
    $scope.addPopupsubmit = false;
    $scope.editPopupsubmit = true;

    angular.forEach($scope.adminUserList, function (value) {
      if (value.userId == userId) {
        $scope.user.id = value.userId;
        $scope.user.firstNameEdit = value.firstName;
        $scope.user.lastNameEdit = value.lastName;
        $scope.user.userNameEdit = value.userName;
        $scope.user.businessCategoryEdit = value.businessCategory;
        $scope.user.mailEdit = value.mail;
        $scope.user.selectedCategoryEdit = value.businessCategory;
        $scope.user.roleId = value.roleId;
        $scope.thumbnail.dataUrledit = value.profilePicture;
        if (value.businessCategory == "ShipMaster") {
          $scope.uiselectshow = false;
          $scope.signleVesselName = true;
          angular.forEach(value.shipProfileInfos, function (val) {
            $scope.user.selectedUsersEdit = val;
          })
        } else {
          $scope.uiselectshow = true;
          $scope.signleVesselName = false;
          angular.forEach(value.shipProfileInfos, function (val) {
            $scope.selectedUsers.push(val);
          })
          $scope.user.selectedUsersEdit = $scope.selectedUsers;
        }

      }
    })
  }

  $scope.viewUser = function (userId) {
    $scope.selectedUsers = [];
    angular.forEach($scope.adminUserList, function (value) {
      if (value.userId == userId) {
        $scope.user.id = value.userId;
        $scope.user.firstNameView = value.firstName;
        $scope.user.lastNameView = value.lastName;
        $scope.user.userNameView = value.userName;
        $scope.user.businessCategoryView = value.businessCategory;
        $scope.user.mailView = value.mail;
        $scope.user.selectedCategoryView = value.businessCategory;
        $scope.thumbnail.dataUrl = value.profilePicture;
        if (value.businessCategory == "ShipMaster") {
          $scope.uiselectshow = false;
          $scope.signleVesselName = true;
          angular.forEach(value.shipProfileInfos, function (val) {
            $scope.user.selectedUsersView = val;
          })
        } else {
          $scope.uiselectshow = true;
          $scope.signleVesselName = false;
          angular.forEach(value.shipProfileInfos, function (val) {
            $scope.selectedUsers.push(val);
          })
          $scope.user.selectedUsersView = $scope.selectedUsers;
        }

      }
    })

  }
  $scope.setPage = function (pageNo) {
    $scope.currentPage = pageNo;
  };

  $scope.pageChanged = function () {
    console.log('Page changed to: ' + $scope.currentPage);
  };

  $scope.setItemsPerPage = function (num) {
    $scope.itemsPerPage = num;
    $scope.currentPage = 1; //reset to first page
  }

  $scope.checkPassword = function (password, confirmPassword) {
    if (password != confirmPassword) {
      $scope.IsMatch = true;
      $scope.isDisabled = true;
    }
    else if (password == confirmPassword) {
      $scope.IsMatch = false;
      $scope.newpassword = password;
      $scope.isDisabled = false;
    }
  }

  $scope.categoryChanges = function (selectedCategory) {
    console.log("inside " + JSON.stringify(selectedCategory));
    if (selectedCategory.roleAliasName == "ShipMaster") {
      $scope.uiselectshow = false;
      $scope.signleVesselName = true;
    } else {
      $scope.uiselectshow = true;
      $scope.signleVesselName = false;
    }
  }
  $scope.close = function () {
    $state.reload();
  }
  $scope.addUserModel = function () {
    $scope.popuptitle = "Add User";
    $scope.selectedUsers = [{}];
    $scope.roleCheckmultiselece = true;
    $scope.password = true;
    $scope.confirmPassword = true;
    $scope.roleCheck = false;
    $scope.addPopupsubmit = true;
    $scope.editPopupsubmit = false;
  }

  $scope.popupClear = function () {
    $scope.user.firstName = "";
    $scope.user.lastName = "";
    $scope.user.mail = "";
    $scope.user.userName = "";
    $scope.user.password = "";
    $scope.user.confirmPassword = "";
    $scope.user.selectedCategory = "";
    $scope.user.selectedUsers = "";
  }

  $scope.popupClearView = function () {
    $scope.user.firstNameView = "";
    $scope.user.lastNameView = "";
    $scope.user.mailView = "";
    $scope.user.userNameView = "";
    $scope.user.selectedCategoryView = "";
    if (angular.isDefined($scope.user.selectedUsersView)) {
      delete $scope.user.selectedUsersView;
    }
  }


  $scope.moreShipClick = function (role, removeUserId) {
    console.log("UserDetails :: " + role + " removeUserId :: " + removeUserId);
    $scope.roleNameForUser = role;
    $scope.userIdForRemove = removeUserId;
  }

  $scope.delShip = function (role, removeUserId, shipId) {
    $scope.loader = true;

    if (role == "ShipMaster") {
      var data = {
        "id": shipId,
        "userId": $scope.userProfileId,
        "shipMasterId": removeUserId
      }
      FunctionalityService.delShipMaster(data).then(function (response) {
        $scope.loader = false;
        if (response.status == 201 || response.status == 200) {
          //getList();
          $timeout(function () {
            toaster.pop('success', response.data.message);
          }, 300);
          $state.reload();
        }
        else if (response.status == 206) {
          toaster.pop('error', response.data.message);
        }
      }, function myError(err) {
        $scope.loader = false;
        console.log("Error response", err);
      });
    }
    if (role == "TechManager") {
      var data = {
        "id": shipId,
        "userId": $scope.userProfileId,
        "techManagerIds": [removeUserId]
      }
      FunctionalityService.delTech(data).then(function (response) {
        $scope.loader = false;
        if (response.status == 201 || response.status == 200) {
          //getList();          
          $timeout(function () {
            toaster.pop('success', response.data.message);
          }, 300);
          $state.reload();
        }
        else if (response.status == 206) {
          toaster.pop('error', response.data.message);
        }
      }, function myError(err) {
        $scope.loader = false;
        console.log("Error response", err);
      });
    }
    if (role == "CommercialManager") {
      var data = {
        "id": shipId,
        "userId": $scope.userProfileId,
        "commercialMasterIds": [removeUserId]
      }
      FunctionalityService.delCom(data).then(function (response) {
        $scope.loader = false;
        if (response.status == 201 || response.status == 200) {
          //getList();          
          $timeout(function () {
            toaster.pop('success', response.data.message);
          }, 300);
          $state.reload();
        }
        else if (response.status == 206) {
          toaster.pop('error', response.data.message);
        }
      }, function myError(err) {
        $scope.loader = false;
        console.log("Error response", err);
      });
    }
  }
}]).directive('customFocus', [function () {
  var FOCUS_CLASS = "custom-focused"; //Toggle a class and style that!
  return {
    restrict: 'A', //Angular will only match the directive against attribute names
    require: 'ngModel',
    link: function (scope, element, attrs, ctrl) {
      ctrl.$focused = false;

      element.bind('focus', function (evt) {
        element.addClass(FOCUS_CLASS);
        scope.$apply(function () { ctrl.$focused = true; });

      }).bind('blur', function (evt) {
        element.removeClass(FOCUS_CLASS);
        scope.$apply(function () { ctrl.$focused = false; });
      });
    }
  }
}]);