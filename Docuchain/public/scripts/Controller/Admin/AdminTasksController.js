// var adminTasks = angular.module('dapp.AdminTasksController', ['angularUtils.directives.dirPagination', 'ui.select', 'ngSanitize', 'moment-picker',]);

// adminTasks.controller('AdminTasksController', ['$scope', '$window', '$location', '$state', '$rootScope', 'FunctionalityService', 'toaster','$timeout', function ($scope, $window, $location, $state, $rootScope, FunctionalityService, toaster,$timeout) {
//   $scope.sessionObject = JSON.parse($window.localStorage.getItem('sessionObject'));

//   $scope.userId = $scope.sessionObject.userId;
//   $scope.roleId = $scope.sessionObject.roleId;
//   $scope.shipId = [];
//   $scope.mindate = moment().add(0, 'day');
//   $scope.currentPage = 1;
//   $scope.viewby = 10;
//   $scope.itemsPerPageForHistory = $scope.viewby;
//   $rootScope.userInfoId = [];
//   $scope.userList = [];
//   $scope.userProfileType = [];
//   $scope.taskUsers = [];
//   $scope.updatetaskUsers = [];
//   $scope.checkboxSelection = 1;
//   $scope.isReadOnly = true;
//   $scope.createTask = {
//     checkboxSelection: "1"
//   };
//   $rootScope.viewTaskUsers = [];
//   $scope.loader = false;

//   $scope.clearCreatetaskInputs = function(createTask){
//     console.log("empty")
//     createTask.shipId = '',
//     createTask.selectedUsers = '',
//     createTask.endDate = '',
//     createTask.taskDetails = '',
//     createTask.taskName = '',
//     createTask.startDate = ''

//   }

//   $scope.myVar = 'tabOne';
//   $scope.change = function (value) {
//     $scope.myVar = value;
//   }

//   $scope.taskListInfoAssignedByUser = function () {
//     $scope.tab = 1;
//     $scope.loader = true;

//     FunctionalityService.getTaskAssignedByUser($scope.userId)
//       .then(function (response) {
//         $scope.loader = false;

//         if (response.status == 200 || response.status == 201) {
//           $scope.message = JSON.stringify(response.data.taskAssignedByUser);

//           $scope.taskAssignedByUsers = response.data.taskAssignedByUser;
//           console.log("task list response", response)

//         }
//         else {
//           toaster.pop('error', response.data.message);
//         }
//       }, function myError(err) {
//         $scope.loader = false;
//         console.log("Error response", err);
//       });

//   }
//   // $scope.taskListInfoAssignedByUser();

//   $scope.taskListInfoAssignedToUser = function () {
//     $scope.loader = true;

//     $scope.tab = 2;
//     FunctionalityService.getTaskAssignedToUser($scope.userId)
//       .then(function (response) {
//         $scope.loader = false;

//         if (response.status == 200 || response.status == 201) {
//           $scope.message = JSON.stringify(response.data.taskAssignedToUser);

//           $scope.taskAssignedToUser = response.data.taskAssignedToUser;
//         }
//         else {

//         }
//       }, function myError(err) {
//         $scope.loader = false;
//         console.log("Error response", err);
//       });

//   }
//   // $scope.taskListInfoAssignedToUser();
//   $scope.viewAssignedTask;

//   $scope.getUpdateTaskId = function (updateId) {
//     $scope.updatetask = updateId;
//     $scope.updateStartDate = updateId.updateDStartDate;
//     $scope.updateEndDate = updateId.updateEndDate;
//     $rootScope.assignedTask = $scope.updatetask;
//     $scope.viewAssignedTask = updateId;

//     angular.forEach($scope.updatetask.userProfileInfos, function (value) {
//       var nameVar = { "userId": value.userId, "userName": value.userName, "firstName": value.firstName, "lastName": value.lastName };
//       $scope.taskUsers.push(nameVar);
//     });
//     $scope.updatetaskUsers = $scope.taskUsers;

//     $scope.userProfileIdList = [];
//     angular.forEach($scope.updatetask.userProfileInfos, function (value, key) {
//       var userinfo = value.userId;
//       $scope.userProfileIdList.push(userinfo);
//     });
//   }
//   $scope.updateShipTask = function (data) {
//     $scope.loader = true;

//     var sdate = data.startDate;
//     var edate = data.endDate;
//     if (data.shipId == undefined) {
//       $scope.checkboxSelection = 2;
//     }

//     if (data.shipId != undefined) {

//     }
//     $scope.userProfileIdList = [];
//     angular.forEach(data.userProfileInfos, function (value, key) {
//       $scope.userProfileIdList.push(value.userId);
//     });
//     var updateTaskData = {
//       "id": $scope.updatetask.taskId,
//       "checkboxSelectionId": $scope.checkboxSelection,
//       "createdBy": data.createdBy,
//       "shipId": data.shipId,
//       "taskName": data.taskName,
//       "taskDetails": data.taskDetails,
//       "startDate": data.startDate,
//       "endDate": data.endDate,
//       "userProfileIds": $scope.userProfileIdList,
//       "loginId": $scope.userId,
//       "taskStatusId" : data.taskStatusId

//     }
//     FunctionalityService.updateShipRelatedTask(JSON.stringify(updateTaskData))
//       .then(function (response) {
//         $scope.loader = false;

//         if (response.data.status == "Success") {
//           $('#updatetask').modal('hide');
//           $timeout(function () {
//             toaster.success({ title: response.data.message });
//             // toaster.pop('success', response.data.message);
//           }, 300);
//           $state.reload();

//         }
//         else {
//           //toaster.clear();
//           toaster.pop('error', response.data.message);

//         }
//       }, function myError(err) {
//         $scope.loader = false;
//         console.log("Error response", err);
//       });
//   }

//   $scope.createShipTaskId = function (createTask) {
//     $rootScope.createShipId = createTask.id;
//   }
//   $scope.createShipTask = function (createTask) {
//     $scope.loader = true;

//     $scope.userIdSelected = [];
//     angular.forEach(createTask.selectedUsers, function (value) {
//       $scope.userIdSelected.push(value.userId);

//     })

//     if ($scope.createTask.checkboxSelection == 1) {
//       var postData = { "shipId": $rootScope.createShipId, "checkboxSelectionId": createTask.checkboxSelection, "createdBy": $scope.userId, "taskName": createTask.taskName, "taskDetails": createTask.taskDetails, "startDate": createTask.startDate, "endDate": createTask.endDate, "userProfileIds": $scope.userIdSelected }

//     } else {
//       var postData = { "checkboxSelectionId": createTask.checkboxSelection, "createdBy": $scope.userId, "taskName": createTask.taskName, "taskDetails": createTask.taskDetails, "startDate": createTask.startDate, "endDate": createTask.endDate, "userProfileIds": $scope.userIdSelected }
//     }
//     FunctionalityService.createShipRelatedTask(postData)
//       .then(function (response) {
//         $scope.loader = false;

//         if (response.status == 200) {
//           $('#createtsk').modal('hide');
//           $state.reload();
//           setTimeout(function () {

//             toaster.pop('success', response.data.message);
//           }, 500);
//         }
//         else {

//           toaster.pop('error', response.data.message);
//         }
//       }, function myError(err) {
//         $scope.loader = false;
//         console.log("Error response", err);
//       });
//   }
//   //End  createShipTask

//   //view task Start

//   $scope.getViewTaskById = function (taskAssignedByUser) {
//     $rootScope.viewTaskByUsers = taskAssignedByUser;
//   }

//   //view task End
//   $scope.getViewTaskss = function (taskId) {
//     $scope.loader = true;

//     FunctionalityService.getTaskStatus(taskId).then(function (response) {
//       $scope.loader = false;

//       if (response.data.status == "Success") {
//         $rootScope.viewTaskUsers = response.data.statusOfTask;
//       }
//       else {
//         toaster.pop('error', response.data.message);
//       }

//     }, function myError(err) {
//       $scope.loader = false;
//       console.log("Error response", err);
//     });

//   }

//   //Delete Task Based on Id

//   $rootScope.deleteTaskId;
//   $scope.getDeleteTaskId = function (id) {
//     $rootScope.deleteTaskId = id;
//     $scope.deleteTaskId = id;

//   }
//   $scope.cancelInput = function (cancelData) {
//     $state.reload();
//     $scope.taskListInfoAssignedByUser();
//   }
//   $scope.deleteTaskById = function () {
//     $scope.loader = true;

//     data = {
//       "taskId": $rootScope.deleteTaskId,
//       "createdBy": $scope.userId
//     }

//     FunctionalityService.deleteTask(data).then(function (response) {
//       if (response.data.status == "Success") {
//         $scope.loader = false;

//         $('#delete').modal('hide');
//         toaster.pop('success', response.data.message);
//         setTimeout(function () {
//           $state.reload();
//           //toaster.success({ title: response.data.message });
//         }, 1000);
//       }
//       else {
//         $('#delete').modal('hide');

//         toaster.pop('error', response.data.message);

//         // toaster.error({ title: response.data.message });
//       }

//     }, function myError(err) {
//       $scope.loader = false;
//       console.log("Error response", err);
//     });
//   }

//   //End Delete Task Based On Id

//   // get ship profile list starting
//   $scope.getShipProfileList = function () {
//     $scope.loader = true;

//     FunctionalityService.getShipProfileList($scope.userId).then(function (response) {
//       $scope.loader = false;

//       if (response.status == 200) {
//         $scope.shipList = response.data.shipProfileList;

//       }
//     }, function myError(err) {
//       $scope.loader = false;
//       console.log("Error response", err);
//     });
//   }
//   // $scope.getShipProfileList();
//   //get ship list based on user id end

//   // get user profile list based on Id start
//   $scope.getUserProfileList = function () {
//     $scope.loader = true;

//     FunctionalityService.getUserProfileList($scope.userId).then(function (response) {
//       $scope.loader = false;

//       if (response.status == 200) {
//         $scope.userLists = response.data.userList;

//       }
//     }, function myError(err) {
//       $scope.loader = false;
//       console.log("Error response", err);
//     });
//   }
//   $scope.getUserProfileList();

//   //get user profile list end

//   //get status list

//   FunctionalityService.taskStatusList()
//     .then(function (response) {
//       $scope.statuslist = response.data.statusAll;
//       $scope.message = JSON.stringify(response.data.statusAll);
//       $scope.statusAll = response.data.statusAll;
//       $scope.totalItems = $scope.statusAll.length;
//       $scope.prograssing = false;
//     }, function myError(err) {
//       $scope.loader = false;
//       console.log("Error response", err);
//     });

//   //End get status list

//   //start status update
//   $scope.updateStatusWithRemark = function (assignedTasks) {
//     $scope.loader = true;

//     var string = assignedTasks.taskStatus,
//     substring = "{";
//   var taskStatusJSON = string.includes(substring);
//   if (taskStatusJSON == true) {
//     $scope.jsontaskStatus = JSON.parse(assignedTasks.taskStatus);
//     $scope.taskStatusId = $scope.jsontaskStatus.taskStatusId
//   }
//   else {
//     $scope.taskStatusId = assignedTasks.taskStatusId;
//   }

//     var taskData = { "createdBy": $scope.userId, "loginId": $scope.userId, "taskStatusId": $scope.taskStatusId, "taskId": assignedTasks.taskId, "userRemarks": assignedTasks.userRemarks };
//     FunctionalityService.updateStatusWithRemarks(taskData)
//       .then(function (response) {
//         $scope.loader = false;

//         if (response.data.status == "Success") {
//           $scope.taskListInfoAssignedByUser();
//           $('#viewtaskaty').modal('hide');
//           toaster.pop('success', response.data.message);
//           setTimeout (function () {
//             $scope.tab = 2;
//             $state.reload();
//           }, 1000);
//         }
//         else {
//           toaster.pop('error', response.data.message);
//         }

//       }, function myError(err) {
//         $scope.loader = false;
//         console.log("Error response", err);
//       });
//   }
//   $scope.closeOrCancel = function () {
//     $state.reload();
//   }
//   //end update status

//   $scope.setPage = function (pageNo) {
//     $scope.currentPage = pageNo;
//   };

//   $scope.pageChanged = function () {
//   };

//   $scope.setItemsPerPage = function (num) {
//     $scope.itemsPerPage = num;
//     $scope.currentPage = 1; //reset to first page
//   }

// }])
//   .directive('customFocus', [function () {
//     var FOCUS_CLASS = "custom-focused"; //Toggle a class and style that!
//     return {
//       restrict: 'A', //Angular will only match the directive against attribute names
//       require: 'ngModel',
//       link: function (scope, element, attrs, ctrl) {
//         ctrl.$focused = false;

//         element.bind('focus', function (evt) {
//           element.addClass(FOCUS_CLASS);
//           scope.$apply(function () { ctrl.$focused = true; });

//         }).bind('blur', function (evt) {
//           element.removeClass(FOCUS_CLASS);
//           scope.$apply(function () { ctrl.$focused = false; });
//         });
//       }
//     }

//   }]);

var adminTasks = angular.module('dapp.AdminTasksController', [
  'angularUtils.directives.dirPagination',
  'ui.select',
  'ngSanitize',
  'moment-picker',
]);

adminTasks
  .controller('AdminTasksController', [
    '$scope',
    '$window',
    '$location',
    '$state',
    '$rootScope',
    'FunctionalityService',
    'toaster',
    '$timeout',
    function (
      $scope,
      $window,
      $location,
      $state,
      $rootScope,
      FunctionalityService,
      toaster,
      $timeout
    ) {
      $scope.sessionObject = JSON.parse(
        $window.localStorage.getItem('sessionObject')
      );

      $scope.userId = $scope.sessionObject.userId;
      $scope.roleId = $scope.sessionObject.roleId;
      $scope.shipId = [];
      $scope.mindate = moment().add(0, 'day');
      $scope.currentPage = 1;
      $scope.viewby = 10;
      $scope.itemsPerPageForHistory = $scope.viewby;
      $rootScope.userInfoId = [];
      $scope.userList = [];
      $scope.userProfileType = [];
      $scope.taskUsers = [];
      $scope.updatetaskUsers = [];
      $scope.checkboxSelection = 1;
      $scope.isReadOnly = true;
      $scope.createTask = {
        checkboxSelection: '1',
      };
      $rootScope.viewTaskUsers = [];
      $scope.loader = false;

      $scope.clearCreatetaskInputs = function (createTask) {
        console.log('empty');
        ((createTask.shipId = ''),
          (createTask.selectedUsers = ''),
          (createTask.endDate = ''),
          (createTask.taskDetails = ''),
          (createTask.taskName = ''),
          (createTask.startDate = ''));
      };

      $scope.myVar = 'tabOne';
      $scope.change = function (value) {
        $scope.myVar = value;
      };

      $scope.taskListInfoAssignedByUser = function () {
        $scope.tab = 1;
        $scope.loader = true;

        FunctionalityService.getTaskAssignedByUser($scope.userId).then(
          function (response) {
            $scope.loader = false;

            if (response.status == 200 || response.status == 201) {
              $scope.message = JSON.stringify(response.data.taskAssignedByUser);

              $scope.taskAssignedByUsers = response.data.taskAssignedByUser;
              console.log('task list response', response);
            } else {
              toaster.pop('error', response.data.message);
            }
          },
          function myError(err) {
            $scope.loader = false;
            console.log('Error response', err);
          }
        );
      };
      // $scope.taskListInfoAssignedByUser();

      $scope.taskListInfoAssignedToUser = function () {
        $scope.loader = true;

        $scope.tab = 2;
        FunctionalityService.getTaskAssignedToUser($scope.userId).then(
          function (response) {
            $scope.loader = false;

            if (response.status == 200 || response.status == 201) {
              $scope.message = JSON.stringify(response.data.taskAssignedToUser);

              $scope.taskAssignedToUser = response.data.taskAssignedToUser;
            } else {
            }
          },
          function myError(err) {
            $scope.loader = false;
            console.log('Error response', err);
          }
        );
      };
      // $scope.taskListInfoAssignedToUser();
      $scope.viewAssignedTask;

      $scope.getUpdateTaskId = function (updateId) {
        //$scope.updatetask = updateId;
        $scope.updatetask = angular.copy(updateId);

        $scope.updateStartDate = updateId.updateDStartDate;
        $scope.updateEndDate = updateId.updateEndDate;
        $rootScope.assignedTask = $scope.updatetask;
        $scope.viewAssignedTask = updateId;

        angular.forEach($scope.updatetask.userProfileInfos, function (value) {
          var nameVar = {
            userId: value.userId,
            userName: value.userName,
            firstName: value.firstName,
            lastName: value.lastName,
          };
          $scope.taskUsers.push(nameVar);
        });
        $scope.updatetaskUsers = $scope.taskUsers;

        $scope.userProfileIdList = [];
        angular.forEach(
          $scope.updatetask.userProfileInfos,
          function (value, key) {
            var userinfo = value.userId;
            $scope.userProfileIdList.push(userinfo);
          }
        );
      };
      $scope.updateShipTask = function (data) {
        $scope.loader = true;

        var sdate = data.startDate;
        var edate = data.endDate;
        if (data.shipId == undefined) {
          $scope.checkboxSelection = 2;
        }

        if (data.shipId != undefined) {
        }
        $scope.userProfileIdList = [];
        angular.forEach(data.userProfileInfos, function (value, key) {
          $scope.userProfileIdList.push(value.userId);
        });
        var updateTaskData = {
          id: $scope.updatetask.taskId,
          checkboxSelectionId: $scope.checkboxSelection,
          createdBy: data.createdBy,
          shipId: data.shipId,
          taskName: data.taskName,
          taskDetails: data.taskDetails,
          startDate: data.startDate,
          endDate: data.endDate,
          userProfileIds: $scope.userProfileIdList,
          loginId: $scope.userId,
          taskStatusId: data.taskStatusId,
        };
        FunctionalityService.updateShipRelatedTask(
          JSON.stringify(updateTaskData)
        ).then(
          function (response) {
            $scope.loader = false;

            if (response.data.status == 'Success') {
              $('#updatetask').modal('hide');
              $timeout(function () {
                toaster.success({ title: response.data.message });
                // toaster.pop('success', response.data.message);
              }, 300);
              $state.reload();
            } else {
              //toaster.clear();
              toaster.pop('error', response.data.message);
            }
          },
          function myError(err) {
            $scope.loader = false;
            console.log('Error response', err);
          }
        );
      };

      $scope.createShipTaskId = function (createTask) {
        $rootScope.createShipId = createTask.id;
      };
      $scope.createShipTask = function (createTask) {
        $scope.loader = true;

        $scope.userIdSelected = [];
        angular.forEach(createTask.selectedUsers, function (value) {
          $scope.userIdSelected.push(value.userId);
        });

        if ($scope.createTask.checkboxSelection == 1) {
          var postData = {
            shipId: $rootScope.createShipId,
            checkboxSelectionId: createTask.checkboxSelection,
            createdBy: $scope.userId,
            taskName: createTask.taskName,
            taskDetails: createTask.taskDetails,
            startDate: createTask.startDate,
            endDate: createTask.endDate,
            userProfileIds: $scope.userIdSelected,
          };
        } else {
          var postData = {
            checkboxSelectionId: createTask.checkboxSelection,
            createdBy: $scope.userId,
            taskName: createTask.taskName,
            taskDetails: createTask.taskDetails,
            startDate: createTask.startDate,
            endDate: createTask.endDate,
            userProfileIds: $scope.userIdSelected,
          };
        }
        FunctionalityService.createShipRelatedTask(postData).then(
          function (response) {
            $scope.loader = false;

            if (response.status == 200) {
              $('#createtsk').modal('hide');
              $state.reload();
              setTimeout(function () {
                toaster.pop('success', response.data.message);
              }, 500);
            } else {
              toaster.pop('error', response.data.message);
            }
          },
          function myError(err) {
            $scope.loader = false;
            console.log('Error response', err);
          }
        );
      };
      //End  createShipTask

      //view task Start

      $scope.getViewTaskById = function (taskAssignedByUser) {
        $rootScope.viewTaskByUsers = taskAssignedByUser;
      };

      //view task End
      $scope.getViewTaskss = function (taskId) {
        $scope.loader = true;

        FunctionalityService.getTaskStatus(taskId).then(
          function (response) {
            $scope.loader = false;

            if (response.data.status == 'Success') {
              $rootScope.viewTaskUsers = response.data.statusOfTask;
            } else {
              toaster.pop('error', response.data.message);
            }
          },
          function myError(err) {
            $scope.loader = false;
            console.log('Error response', err);
          }
        );
      };

      //Delete Task Based on Id

      $rootScope.deleteTaskId;
      $scope.getDeleteTaskId = function (id) {
        $rootScope.deleteTaskId = id;
        $scope.deleteTaskId = id;
      };
      $scope.cancelInput = function (cancelData) {
        $state.reload();
        $scope.taskListInfoAssignedByUser();
      };
      $scope.deleteTaskById = function () {
        $scope.loader = true;

        data = {
          taskId: $rootScope.deleteTaskId,
          createdBy: $scope.userId,
        };

        FunctionalityService.deleteTask(data).then(
          function (response) {
            if (response.data.status == 'Success') {
              $scope.loader = false;

              $('#delete').modal('hide');
              toaster.pop('success', response.data.message);
              setTimeout(function () {
                $state.reload();
                //toaster.success({ title: response.data.message });
              }, 1000);
            } else {
              $('#delete').modal('hide');

              toaster.pop('error', response.data.message);

              // toaster.error({ title: response.data.message });
            }
          },
          function myError(err) {
            $scope.loader = false;
            console.log('Error response', err);
          }
        );
      };

      //End Delete Task Based On Id

      // get ship profile list starting
      $scope.getShipProfileList = function () {
        $scope.loader = true;

        FunctionalityService.getShipProfileList($scope.userId).then(
          function (response) {
            $scope.loader = false;

            if (response.status == 200) {
              $scope.shipList = response.data.shipProfileList;
            }
          },
          function myError(err) {
            $scope.loader = false;
            console.log('Error response', err);
          }
        );
      };
      // $scope.getShipProfileList();
      //get ship list based on user id end

      // get user profile list based on Id start
      $scope.getUserProfileList = function () {
        $scope.loader = true;

        FunctionalityService.getUserProfileList($scope.userId).then(
          function (response) {
            $scope.loader = false;

            if (response.status == 200) {
              $scope.userLists = response.data.userList;
            }
          },
          function myError(err) {
            $scope.loader = false;
            console.log('Error response', err);
          }
        );
      };
      $scope.getUserProfileList();

      //get user profile list end

      //get status list

      FunctionalityService.taskStatusList().then(
        function (response) {
          $scope.statuslist = response.data.statusAll;
          $scope.message = JSON.stringify(response.data.statusAll);
          $scope.statusAll = response.data.statusAll;
          $scope.totalItems = $scope.statusAll.length;
          $scope.prograssing = false;
        },
        function myError(err) {
          $scope.loader = false;
          console.log('Error response', err);
        }
      );

      //End get status list

      //start status update
      $scope.updateStatusWithRemark = function (assignedTasks) {
        $scope.loader = true;

        var string = assignedTasks.taskStatus,
          substring = '{';
        var taskStatusJSON = string.includes(substring);
        if (taskStatusJSON == true) {
          $scope.jsontaskStatus = JSON.parse(assignedTasks.taskStatus);
          $scope.taskStatusId = $scope.jsontaskStatus.taskStatusId;
        } else {
          $scope.taskStatusId = assignedTasks.taskStatusId;
        }

        var taskData = {
          createdBy: $scope.userId,
          loginId: $scope.userId,
          taskStatusId: $scope.taskStatusId,
          taskId: assignedTasks.taskId,
          userRemarks: assignedTasks.userRemarks,
        };
        FunctionalityService.updateStatusWithRemarks(taskData).then(
          function (response) {
            $scope.loader = false;

            if (response.data.status == 'Success') {
              $scope.taskListInfoAssignedByUser();
              $('#viewtaskaty').modal('hide');
              toaster.pop('success', response.data.message);
              setTimeout(function () {
                $scope.tab = 2;
                $state.reload();
              }, 1000);
            } else {
              toaster.pop('error', response.data.message);
            }
          },
          function myError(err) {
            $scope.loader = false;
            console.log('Error response', err);
          }
        );
      };
      $scope.closeOrCancel = function () {
        $state.reload();
      };
      //end update status

      $scope.setPage = function (pageNo) {
        $scope.currentPage = pageNo;
      };

      $scope.pageChanged = function () {};

      $scope.setItemsPerPage = function (num) {
        $scope.itemsPerPage = num;
        $scope.currentPage = 1; //reset to first page
      };
    },
  ])
  .directive('customFocus', [
    function () {
      var FOCUS_CLASS = 'custom-focused'; //Toggle a class and style that!
      return {
        restrict: 'A', //Angular will only match the directive against attribute names
        require: 'ngModel',
        link: function (scope, element, attrs, ctrl) {
          ctrl.$focused = false;

          element
            .bind('focus', function (evt) {
              element.addClass(FOCUS_CLASS);
              scope.$apply(function () {
                ctrl.$focused = true;
              });
            })
            .bind('blur', function (evt) {
              element.removeClass(FOCUS_CLASS);
              scope.$apply(function () {
                ctrl.$focused = false;
              });
            });
        },
      };
    },
  ]);
