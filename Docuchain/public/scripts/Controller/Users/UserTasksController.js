var userTasks = angular.module('dapp.UserTasksController', ['angularUtils.directives.dirPagination', 'moment-picker',]);

userTasks.controller('UserTasksController', ['$scope', '$window', '$location', '$state', '$rootScope', 'FunctionalityService', 'toaster','$timeout', function ($scope, $window, $location, $state, $rootScope, FunctionalityService, toaster, $timeout) {
  $scope.sessionObject = JSON.parse($window.localStorage.getItem('sessionObject'));

  $scope.userId = $scope.sessionObject.userId;
  $scope.currentPage = 1;
  $scope.viewby = 10;
  $scope.userProfileType = [];
  $rootScope.userInfoId = [];
  $scope.userList = [];
  $scope.taskUsers = [];
  $scope.updatetaskUsers = [];
  $scope.checkboxSelection = 1;
  $scope.updatetask = {};
  $scope.loader = false;
  $scope.mindate = moment().add(0, 'day');
  $scope.itemsPerPageForHistory = $scope.viewby;
  $scope.myVar = 'tabOne';
  $rootScope.selected = 5;
  $scope.change = function (value) {
    $scope.myVar = value;
  }

  $scope.userTaskListAssignedByUser = function () {
    $scope.loader = true;
    $scope.tab = 1;
    FunctionalityService.getTaskAssignedByUser($scope.userId)
      .then(function (response) {
        $scope.loader = false;
        if (response.status == 200 || response.status == 201) {
          $scope.message = JSON.stringify(response.data.taskAssignedByUser);
          $scope.taskAssignedByUser = response.data.taskAssignedByUser;

        }
        else {
          toaster.pop('error', response.data.message);

          // toaster.error({ title: response.data.message });
        }
      }, function myError(err) {
        $scope.loader = false;
        console.log("Error response", err);
      });

  }
  //User TASK ASSIGNED TO YOU
  $scope.userTaskListAssignedToYou = function () {
    $scope.loader = true;
    $scope.tab = 2;
    FunctionalityService.getTaskAssignedToUser($scope.userId)
      .then(function (response) {
        $scope.loader = false;
        if (response.status == 200 || response.status == 201) {
          $scope.message = JSON.stringify(response.data.taskAssignedToUser);
          $scope.taskAssignedToUser = response.data.taskAssignedToUser;

        }
        else {
          toaster.pop('error', response.data.message);

          //toaster.error({ title: response.data.message });
        }
      }, function myError(err) {
        $scope.loader = false;
        console.log("Error response", err);
      });

  }
  // $scope.taskListInfoAssignedToUser();


  //createTask start

  $scope.createTaskForUser = function (createTask) {
    $scope.loader = true;

    $scope.userIdSelected = [];
    angular.forEach(createTask.selectedUsers, function (value) {
      $scope.userIdSelected.push(value.userId);

    })
    // $scope.userList = [];
    // angular.forEach($rootScope.userInfoId, function (value, key) {
    //   $scope.userList.push(value.id);
    // });
    if ($scope.createTask.checkboxSelection == 1) {
      var postData = { "shipId": createTask.shipId, "checkboxSelectionId": createTask.checkboxSelection, "createdBy": $scope.userId, "taskName": createTask.taskName, "taskDetails": createTask.taskDetails, "startDate": createTask.startDate, "endDate": createTask.endDate, "userProfileIds": $scope.userIdSelected }

    } else {
      var postData = { "checkboxSelectionId": createTask.checkboxSelection, "createdBy": $scope.userId, "taskName": createTask.taskName, "taskDetails": createTask.taskDetails, "startDate": createTask.startDate, "endDate": createTask.endDate, "userProfileIds": $scope.userIdSelected }
    }
    FunctionalityService.createShipRelatedTask(postData)
      .then(function (response) {
        $scope.loader = false;

        if (response.status == 200) {
          $('#createtsk').modal('hide');
          $state.reload();
          $timeout(function () {
            //toaster.clear();
             toaster.success({ title: response.data.message });
            //toaster.pop('success', response.data.message);
          }, 2000);
        }
        else {
          //toaster.clear();
          toaster.pop('error', response.data.message);
          //  toaster.error({ title: response.data.message });
        }
      }, function myError(err) {
        $scope.loader = false;
        console.log("Error response", err);
      });
  }
  //End  createShipTask





  $scope.getUpdateTaskId = function (updateId) {
    $scope.updatetask = updateId;
    $scope.updatetask.shipName = $scope.updatetask.shipName;
    $scope.userProfileId = [];
    angular.forEach(updateId.userProfileInfos, function (value) {
      $scope.userProfileId.push(value);
    });
    $scope.updatetask.updatetaskUsers = $scope.userProfileId;
    updatetask.updatetaskUsers = $scope.taskUsers;
    $scope.userProfileIdList = [];
    angular.forEach($scope.updatetask.userProfileInfos, function (value, key) {
      var userinfo = value.userId;
      $scope.userProfileIdList.push(userinfo);
    });
  }
  $scope.updateShipTask = function (data) {
    $scope.loader = true;

    var sdate = data.startDate;
    var edate = data.endDate;
    if (data.shipId == undefined) {
      $scope.checkboxSelection = 2;
    }
    // if ($scope.updateSDate != undefined) {
    //   sdate = $scope.updateSDate;
    // }
    // if ($scope.updateEDate != undefined) {
    //   edate = $scope.updateEDate;
    // }
    $scope.userProfileIdList = [];
    angular.forEach(data.updatetaskUsers, function (value) {
      $scope.userProfileIdList.push(value.userId);
    });

    var updateTaskData = {
      "id": $scope.updatetask.taskId,
      "checkboxSelectionId": $scope.checkboxSelection,
      "createdBy": data.createdBy,
      "shipId": data.shipId,
      "taskName": data.taskName,
      "taskDetails": data.taskDetails,
      "startDate": data.startDate,
      "endDate": data.endDate,
      "userProfileIds": $scope.userProfileIdList,
      //"createdBy": "1",
      "taskStatusId": data.taskStatusId,
      "loginId": $scope.userId

    }
    FunctionalityService.updateShipRelatedTask(JSON.stringify(updateTaskData))
      .then(function (response) {
        $scope.loader = false;
        if (response.data.status == "Success") {
          $('#updatetask').modal('hide');
          $timeout(function () {           
            toaster.pop('success', response.data.message);
          }, 2000);
          $scope.userTaskListAssignedByUser();
          // toaster.success({ title: response.data.message });
          //}, 2000);
        }
        else {
          // toaster.clear();
          toaster.pop('error', response.data.message);
        }
      }, function (error) {
        $location.path('/');
        console.log("message :: " + error.data.message);
      });
  }


  // get ship profile list starting
  $scope.getShipProfileList = function () {
    $scope.loader = true;
    FunctionalityService.getShipProfileList($scope.userId).then(function (response) {
      $scope.loader = false;

      if (response.status == 200) {
        $scope.shipList = response.data.shipProfileList;
      }
    }, function (error) {
      console.log("message :: " + error);
    });

  }

  //end get ship profile

  //view task Start by user

  $scope.getViewTaskById = function (taskAssignedByUser) {
    $rootScope.viewTaskByUsers = taskAssignedByUser;
    $scope.userprofile = taskAssignedByUser.userProfileInfos;
  }

  //view task End


  // //view task Start to user

  // $scope.getViewTaskByIdToUser = function (taskAssignedByUser) {
  //   $rootScope.viewTaskByToUsers = taskAssignedByUser;
  //   console.log("inside Task Id :: " + $rootScope.viewTaskByToUsers.id);
  // }

  // //view task End

  //Delete Task Based on Id

  $rootScope.deleteTaskId;
  $scope.getDeleteTaskId = function (id) {
    $rootScope.deleteTaskId = id;
    $scope.deleteTaskId = id;

  }
  $scope.cancelInput = function (cancelData) {
    $('#viewtaskaty').modal('hide');
     $state.reload();  
    $scope.getAssignedTask();
  }
  $scope.deleteTaskById = function () {
    $scope.loader = true;
    data = {
      "taskId": $rootScope.deleteTaskId
    }
    FunctionalityService.deleteTask(data).then(function (response) {
      $scope.loader = false;
      if (response.data.status == "Success") {
        $('#delete').modal('hide');
        $state.reload();
        $timeout(function () {
          toaster.pop('success', response.data.message);
        }, 1000);
      }
      else {
        $('#delete').modal('hide');

        toaster.pop('error', response.data.message);

      }

    }, function (error) {
      console.log("message :: " + error.message);
    });
  }

  //End Delete Task Based On Id
  // get user profile list based on Id start
  $scope.getUserProfileList = function () {
    $scope.loader = true;

    FunctionalityService.getUserProfileList($scope.userId).then(function (response) {
      $scope.loader = false;

      if (response.status == 200) {
        $scope.userLists = response.data.userList;

        // $scope.commercialManagerUsers = response.data.userInfos.commercialManagerInfos;
        // $scope.shipmaster = response.data.userInfos.shipMasterInfos;
        // $scope.techManager = response.data.userInfos.technicalManagerInfos;

        // //iterating 3 arrays
        // angular.forEach($scope.techManager, function (data) {
        //   var name = { "id": data.userId, "userName": data.userName };
        //   $scope.userProfileType.push(name);
        //   console.log("technicalManagerInfos:", $scope.userProfileType);
        // });
        // angular.forEach($scope.commercialManagerUsers, function (data) {
        //   var name = { "id": data.userId, "userName": data.userName };
        //   $scope.userProfileType.push(name);
        //   console.log(" $scope.commercialManagerInfos:::", $scope.userProfileType);
        // });
        // angular.forEach($scope.shipmaster, function (data) {
        //   var name = { "id": data.userId, "userName": data.userName };
        //   $scope.userProfileType.push(name);
        // });

      }
    }, function (error) {
      console.log("message :: " + error);
    });

  }
  //$scope.getUserProfileList();

  //end user profile list


  //get status list

  FunctionalityService.taskStatusList()
    .then(function (response) {
      $scope.loader = false;

      $scope.statuslist = response.data.statusAll;
      $scope.message = JSON.stringify(response.data.statusAll);
      $scope.statusAll = response.data.statusAll;
      $scope.totalItems = $scope.statusAll.length;
      $scope.prograssing = false;
    }, function (error) {
      $state.go('dapp.taskManagement');
      $scope.status = 'Unable to Create Room: ';
      console.log("message :: " + error.message);
    });
  //End get status list


  //start status update
  $scope.updateStatusWithRemarkToUsers = function (assignedTasks) {
    $scope.loader = true;

    var taskData = { "createdBy": $scope.userId, "taskStatusId": assignedTasks.taskStatus, "taskId": assignedTasks.taskId, "userRemarks": assignedTasks.userRemarks, "loginId": $scope.userId };
    FunctionalityService.updateStatusWithRemarks(taskData)
      .then(function (response) {
        $scope.loader = false;
        if (response.data.status == "Success") {
          // $scope.taskListInfoAssignedByUser();
          $('#viewtaskaty').modal('hide');
          $state.reload();
          $timeout(function () {
            //toaster.clear();
            $scope.tab = 2;
            toaster.pop('success', response.data.message);

            // toaster.success({ title: response.data.message });
          }, 1000);
        }
        else {
          //  toaster.clear();
          toaster.pop('error', response.data.message);

          //toaster.error({ title: response.data.message });
        }

      }, function (error) {
        $scope.status = 'Unable to Create Room: ';
        console.log("message :: " + error.message);
      });
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
}])
  .directive('customFocus', [function () {
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