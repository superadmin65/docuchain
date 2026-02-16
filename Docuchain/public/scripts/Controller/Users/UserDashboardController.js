var userDashboard = angular.module('dapp.UserDashboardController', [
  'angularUtils.directives.dirPagination',
  '$idle',
]);

userDashboard.controller('UserDashboardController', [
  '$scope',
  '$window',
  '$location',
  '$state',
  '$rootScope',
  'toaster',
  '$timeout',
  'FunctionalityService',
  // 'uiGmapGoogleMapApi',
  // 'uiGmapIsReady',
  '$idle',
  function (
    $scope,
    $window,
    $location,
    $state,
    $rootScope,
    toaster,
    $timeout,
    FunctionalityService,
    // uiGmapGoogleMapApi,
    // uiGmapIsReady,
    $idle
  ) {
    $scope.sessionObject = JSON.parse(
      $window.localStorage.getItem('sessionObject')
    );

    $scope.roleId = $scope.sessionObject.roleId;

    $scope.userProfileId = $window.localStorage.getItem('userId');
    $scope.profilePictures = $window.localStorage.getItem('profilePicture');
    $scope.shipProfileInfos = $window.localStorage.getItem('shipProfileInfos');
    $scope.notificationId = null;
    $scope.deleteNotificationList = [];
    $scope.currentPage = 1;
    $scope.shipProfileList = [];
    $scope.shipProfileListLength = $scope.shipProfileList.length;
    $scope.sidebarList = [];
    $scope.editShow = true;
    $scope.resetShow = false;
    $scope.profilePicture;
    $scope.loader = false;
    $rootScope.markers = [];
    $rootScope.selected;
    var latitudehome = 1.3521;
    var longitutehome = 103.8198;

    $scope.$on('$viewContentLoaded', function () {
      var data = { userId: $scope.userProfileId, filterByDay: 'Renewel' };
      FunctionalityService.getDocumentNotification(data).then(
        function mySuccess(response) {
          $scope.notificationList = response.data.userList;
          $scope.notificationListFielter = $scope.notificationList;
          console.info(
            'response.data.userList:',
            JSON.stringify($scope.notificationListFielter)
          );
        },
        function myError(err) {
          console.log('Error response');
        }
      );
    });
    $scope.$on('$viewContentLoaded', function () {
      var data = { userId: $scope.userProfileId, filterByDay: 'Lastweek' };
      FunctionalityService.getDocumentNotification(data).then(
        function mySuccess(response) {
          $scope.notificationListLastweek = response.data.userList;
          $scope.notificationListLastweekFielter =
            $scope.notificationListLastweek;
          console.info(
            'response.data.userListLastweek:',
            JSON.stringify(response.data.userList)
          );
        },
        function myError(err) {
          console.log('Error response');
        }
      );
    });
    $scope.$on('$viewContentLoaded', function () {
      var data = { userId: $scope.userProfileId, filterByDay: 'Lastmonth' };
      FunctionalityService.getDocumentNotification(data).then(
        function mySuccess(response) {
          $scope.notificationListLastmonth = response.data.userList;
          $scope.notificationListLastmonthFielter =
            $scope.notificationListLastmonth;
          console.info(
            'response.data.userListLastmonth:',
            JSON.stringify(response.data.userList)
          );
        },
        function myError(err) {
          console.log('Error response');
        }
      );
    });
    $scope.$on('$viewContentLoaded', function () {
      var data = { userId: $scope.userProfileId, filterByDay: 'Older' };
      FunctionalityService.getDocumentNotification(data).then(
        function mySuccess(response) {
          $scope.notificationListOlder = response.data.userList;
          $scope.notificationListOlderFielter = $scope.notificationListOlder;
          console.info(
            'response.data.userListOlder:',
            JSON.stringify(response.data.userList)
          );
        },
        function myError(err) {
          console.log('Error response');
        }
      );
    });
    $rootScope.select = function (index) {
      console.log('index of the sidebar', index);
      $rootScope.selected = index;
    };
    $scope.thumbnail = {
      dataUrl: '',
    };
    if ($scope.profilePictures != 'undefined') {
      $scope.thumbnail.dataUrl = $scope.profilePictures;
    } else {
      $scope.thumbnail = {
        dataUrl: 'undefined',
      };
    }
    $scope.thumbnail.dataUrl = $scope.profilePictures;
    $scope.fileReaderSupported = window.FileReader != null;
    $scope.uploadFiledp = function (files) {
      if (files != null) {
        var file = files[0];
        if (files[0].size > 2048000) {
          $rootScope.errorFile = document.getElementById(
            'sizeOffile'
          ).innerHTML = 'Picture should be below 1MB';
        } else {
          document.getElementById('sizeOffile').innerHTML = '';
          $rootScope.errorFile = '';
        }
        $scope.profilePicture = file;
        if ($scope.fileReaderSupported && file.type.indexOf('image') > -1) {
          $timeout(function () {
            var fileReader = new FileReader();
            fileReader.readAsDataURL(file);
            fileReader.onload = function (e) {
              $timeout(function () {
                $scope.thumbnail.dataUrl = e.target.result;
                $scope.profilePictures = $scope.thumbnail.dataUrl;
              });
            };
          });
        }
      }
    };

    $scope.openNotification = function (openlink) {
      console.log('open', openlink);
      if (openlink == 'GeoLocationUpload') $state.go('dapp.userDashboard');
      if (openlink == 'New Document ') $state.go('dapp.userDoumentApproval');
      if (openlink == 'Task ') $state.go('dapp.userTasks');
    };

    $scope.openTask = function () {
      $state.go('dapp.userTasks');
    };
    $scope.userDetails = function () {
      $state.go('dapp.userProfile');
      $scope.editShow = true;
      $scope.resetShow = false;
    };

    $scope.editProfile = function () {
      $scope.editShow = true;
      $scope.resetShow = false;
      $state.reload();
    };

    $scope.notificationTypeFilter = function (notificationType) {
      console.log('notificationTypeFielter:', notificationType);
      $scope.notificationListFielter = [];
      $scope.notificationListLastweekFielter = [];
      $scope.notificationListLastmonthFielter = [];
      $scope.notificationListOlderFielter = [];
      angular.forEach($scope.notificationList, function (item) {
        if (notificationType == item.notificationType.trim()) {
          $scope.notificationListFielter.push(item);
        }
      });
      angular.forEach($scope.notificationListLastweek, function (item) {
        if (notificationType == item.notificationType.trim()) {
          $scope.notificationListLastweekFielter.push(item);
        }
      });
      angular.forEach($scope.notificationListLastmonth, function (item) {
        if (notificationType == item.notificationType.trim()) {
          $scope.notificationListLastmonthFielter.push(item);
        }
      });
      angular.forEach($scope.notificationListOlder, function (item) {
        if (notificationType == item.notificationType.trim()) {
          $scope.notificationListOlderFielter.push(item);
        }
      });
      if (notificationType === '') {
        $scope.notificationListOlderFielter = $scope.notificationListOlder;
        $scope.notificationListLastmonthFielter =
          $scope.notificationListLastmonth;
        $scope.notificationListLastweekFielter =
          $scope.notificationListLastweek;
        $scope.notificationListFielter = $scope.notificationList;
      }
    };

    $scope.snoozeOption = function (snooze, notifId) {
      $scope.loader = true;
      console.log('id and snooze', snooze, notifId);
      var data = { snooze: snooze, notificationId: notifId };
      FunctionalityService.snoozeUpdate(data).then(
        function (response) {
          $scope.loader = false;
          if (response.status == 200 || response.status == 201) {
            toaster.pop('success', response.data.message);
            $scope.resetShow = false;
            $scope.editShow = true;
            setTimeout(function () {
              $state.reload();
            }, 1000);
          } else {
            toaster.pop('error', response.data.message);
            $scope.resetShow = true;
            $scope.editShow = false;
          }
        },
        function myError(err) {
          $scope.loader = false;
          console.log('Error response', err);
        }
      );
    };
    $scope.updateProfileSubmit = function (data) {
      $scope.loader = true;

      toaster.clear();
      if ($rootScope.errorFile === 'Picture should be below 1MB') {
        $scope.loader = false;
        toaster.pop('error', $rootScope.errorFile);
      } else {
        $rootScope.errorFile = '';
        var data = {
          userId: $scope.userProfileId,
          firstName: data.firstName,
          lastName: data.lastName,
          mail: data.mail,
        };

        FunctionalityService.editProfileData(data, $scope.profilePicture).then(
          function (response) {
            $scope.loader = false;

            if (response.status == 200 || response.status == 201) {
              toaster.pop('success', response.data.message);
              $scope.message = response.data.userInfos;

              $window.localStorage.setItem(
                'sessionObject',
                JSON.stringify($scope.message)
              );
              $window.localStorage.setItem(
                'userRole',
                $scope.message.businessCategory
              );
              $window.localStorage.setItem('userName', $scope.message.userName);
              $window.localStorage.setItem('userEmail', $scope.message.mail);
              $window.localStorage.setItem('userId', $scope.message.userId);
              $window.localStorage.setItem('roleId', $scope.message.roleId);
              $window.localStorage.setItem('role', $scope.message.role);
              $window.localStorage.setItem(
                'organizationId',
                $scope.message.organizationId
              );
              $window.localStorage.setItem(
                'profilePicture',
                $scope.message.profilePicture
              );

              $scope.resetShow = false;
              $scope.editShow = false;
              setTimeout(function () {
                //$state.reload();
                $window.location.reload();
              }, 1000);
            } else {
              toaster.pop('error', response.data.message);
              $scope.editShow = true;
              $scope.resetShow = false;
            }
          },
          function myError(err) {
            $scope.loader = false;
            console.log('Error response', err);
          }
        );
      }
    };
    $scope.resetPassword = function () {
      $scope.resetShow = true;
      $scope.editShow = false;
    };
    $scope.resetPasswordSubmit = function (data) {
      $scope.loader = true;

      toaster.clear();

      var data = {
        userId: $scope.userProfileId,
        currentPassword: data.currentPassword,
        password: data.password,
        confirmPassword: data.confirmPassword,
      };

      FunctionalityService.resetPswd(data).then(
        function (response) {
          $scope.loader = false;

          if (response.status == 200 || response.status == 201) {
            toaster.pop('success', response.data.message);
            $scope.resetShow = false;
            $scope.editShow = true;
            setTimeout(function () {
              $state.reload();
            }, 1000);
          } else {
            toaster.pop('error', response.data.message);
            $scope.resetShow = true;
            $scope.editShow = false;
          }
        },
        function myError(err) {
          $scope.loader = false;
          console.log('Error response', err);
        }
      );
    };
    $scope.$on('$viewContentLoaded', function () {
      FunctionalityService.geoLocationlist(
        $window.localStorage.getItem('userId')
      ).then(
        function (response) {
          if (response.status == 200) {
            $rootScope.geoLocationlistUser = response.data.userList;
            //console.log("response.data.userList::",JSON.stringify(response.data.userList))
          }
        },
        function (error) {}
      );
    });
    $scope.$on('$viewContentLoaded', function () {
      FunctionalityService.getQuestionAndAnswer().then(
        function (response) {
          if (response.status == 200) {
            $scope.getQuestionAndAnswer = response.data.faqInfos;
            console.log(
              '$scope.getQuestionAndAnswer::',
              JSON.stringify(response.data)
            );
          }
        },
        function (error) {}
      );
    });
    // $scope.map = {center: {latitude: 51.219053, longitude: 4.404418 }, zoom: 14 };
    $scope.$on('$viewContentLoaded', function () {
      // uiGmapGoogleMapApi.then(function (maps) {
      //   //var dogParks = $meteor.collection(DogParks);
      //   // var dogParks = genarateDogParks(2000);
      //   // if ($rootScope.geoLocationlistUser != undefined){
      //   //   var dogParks = genarateDogParks($rootScope.geoLocationlistUser.length)
      //   // }else{
      //   //   var dogParks = 0;
      //   // }
      //   console.log('default values::', latitudehome + '...' + longitutehome);
      //   $scope.map = {
      //     zoom: 4,
      //     bounds: {},
      //     center: {
      //       latitude: latitudehome,
      //       longitude: longitutehome,
      //     },
      //   };
      //   function assignMarkersToMap() {
      //     angular.forEach($rootScope.geoLocationlistUser, function (item) {
      //       if (
      //         item.geoLocationInfos.latitude != undefined &&
      //         item.geoLocationInfos.longitute != undefined
      //       ) {
      //         latitudehome = item.geoLocationInfos.latitude;
      //         longitutehome = item.geoLocationInfos.longitute;
      //         $rootScope.markers.push({
      //           latitude: item.geoLocationInfos.latitude,
      //           longitude: item.geoLocationInfos.longitute,
      //           title: item.shipProfileDTO.shipName,
      //           id: item.geoLocationInfos.id,
      //           updateDate: item.geoLocationInfos.updateDate,
      //           imomap: item.shipProfileDTO.imo,
      //           icon: 'image/shipIcon.png',
      //         });
      //       }
      //     });
      //     console.log('default values::', latitudehome + '...' + longitutehome);
      //     console.log(
      //       'inside assignMarkersToMap::' + JSON.stringify($rootScope.markers)
      //     );
      //     return $rootScope.markers;
      //   }
      //   $scope.markers = [];
      //   // uiGmapIsReady.promise().then(function (instances) {
      //   //   $scope.markers = assignMarkersToMap();
      //   // });
      //   $scope.windowCoords = {};
      //   $scope.onClick = function (marker, eventName, model) {
      //     $scope.map.center.latitude = model.latitude;
      //     $scope.map.center.longitude = model.longitude;
      //     $scope.map.zoom = 11;
      //     $scope.windowCoords.latitude = model.latitude;
      //     $scope.windowCoords.longitude = model.longitude;
      //     $scope.longitude = $scope.windowCoords.longitude;
      //     $scope.latitude = $scope.windowCoords.latitude;
      //     $scope.parkName = model.title;
      //     $scope.updateDate = model.updateDate;
      //     $scope.imo = model.imomap;
      //     $scope.show = true;
      //   };
      //   $scope.closeClick = function () {
      //     $scope.show = false;
      //   };
      //   $scope.options = {
      //     scrollwheel: false,
      //   };
      //   $scope.show = false;
      // });
    });

    // function genarateDogParks(count) {
    //   var vals = [];
    //   for (var i = 0; i < count; i++) {
    //     vals.push({
    //       latitude: $scope.geoLocationlist[i].geoLocationInfos.latitude,
    //       longitude: $scope.geoLocationlist[i].geoLocationInfos.longitute,
    //       _id: i,
    //       // name: 'Dog Park #' + i
    //       name: $scope.geoLocationlist[i].shipProfileDTO.shipName,
    //       imo: $scope.geoLocationlist[i].shipProfileDTO.imo,
    //       updateDate: $scope.geoLocationlist[i].geoLocationInfos.updateDate,

    //     });
    //   }
    //   return vals;
    // }

    function getRandomArbitrary(min, max) {
      return Math.random() * (max - min) + min;
    }

    $scope.getAllVesselListAndCount = function () {
      $scope.loader = true;

      $scope.buttonToaster();
      var userId = $scope.sessionObject.userId;
      FunctionalityService.getViewShipProfile(userId).then(
        function mySuccess(response) {
          $scope.loader = false;
          if (response.status == 200) {
            $scope.shipProfileList = response.data.shipProfileList;
            $scope.shipProfileListLength = $scope.shipProfileList.length;
          }
        },
        function myError(err) {
          $scope.loader = false;
          console.log('Error response', err);
        }
      );
    };

    $scope.buttonToaster = function () {
      // toaster.error({ title:"eerroorajvasdffalflalfallafj"});
    };

    $scope.shipMasterSidebar = [
      {
        id: 1,
        url: 'dapp.userDashboard',
        color: 'imgclrs clr972323',
        status: 'active',
        name: 'Dashboard',
        imgName: 'dashboardIcon.png',
      },
      {
        id: 2,
        url: 'dapp.userVesselDocumentEBD',
        color: 'imgclrs clrff8830',
        status: 'active',
        name: 'Vessel Documents',
        imgName: 'vesselDocumentIcon.png',
      },
      {
        id: 3,
        url: 'dapp.userDoumentApproval',
        color: 'imgclrs clr187e03',
        status: 'active',
        name: 'Document Approval',
        imgName: 'documentApprovalIcon.png',
      },
      {
        id: 4,
        url: 'dapp.userMyWorkspaceList',
        color: 'imgclrs clr004283',
        status: 'active',
        name: 'My Workspace',
        imgName: 'workspaceIcon.png',
      },
      {
        id: 5,
        url: 'dapp.userTasks',
        color: 'imgclrs clr8b0ce8',
        status: 'active',
        name: 'Tasks',
        imgName: 'taskIcon.png',
      },
    ];

    $scope.otherUserSidebar = [
      {
        id: 1,
        url: 'dapp.userDashboard',
        color: 'imgclrs clr972323',
        status: 'active',
        name: 'Dashboard',
        imgName: 'dashboardIcon.png',
      },
      {
        id: 2,
        url: 'dapp.userVesselDocument',
        color: 'imgclrs clrff8830',
        status: 'active',
        name: 'Vessel Documents',
        imgName: 'vesselDocumentIcon.png',
      },
      {
        id: 3,
        url: 'dapp.userDoumentApproval',
        color: 'imgclrs clr187e03',
        status: 'active',
        name: 'Document Approval',
        imgName: 'documentApprovalIcon.png',
      },
      {
        id: 4,
        url: 'dapp.userMyWorkspace',
        color: 'imgclrs clr004283',
        status: 'active',
        name: 'My Workspace',
        imgName: 'workspaceIcon.png',
      },
      {
        id: 5,
        url: 'dapp.userUserExtension',
        color: 'imgclrs clr005cf7',
        status: 'active',
        name: 'User Extension',
        imgName: 'userExtensionIcon.png',
      },
      {
        id: 6,
        url: 'dapp.userTasks',
        color: 'imgclrs clr8b0ce8',
        status: 'active',
        name: 'Tasks',
        imgName: 'taskIcon.png',
      },
    ];

    if ($scope.sessionObject.roleId === 3) {
      $scope.sidebarList = $scope.shipMasterSidebar;
    }
    if ($scope.sessionObject.roleId != 3) {
      $scope.sidebarList = $scope.otherUserSidebar;
    }

    $scope.clearGeoLocation = function () {
      $scope.latitude = '';
      $scope.longitute = '';
    };
    $scope.addGeoLocation = function () {
      $scope.loader = true;

      angular.forEach($scope.sessionObject.shipProfileInfos, function (val) {
        $scope.shipId = val.id;
      });
      var addGeoObject = {
        latitude: $scope.latitude,
        longitute: $scope.longitute,
        userId: $scope.sessionObject.userId,
        shipId: $scope.shipId,
      };
      FunctionalityService.addGeoLocation(addGeoObject).then(
        function mySuccess(response) {
          $scope.loader = false;
          if (response.status == 200) {
            $state.reload();
            $timeout(function () {
              toaster.success({ title: response.data.message });
            }, 1000);
          }
        },
        function myError(err) {
          $scope.loader = false;
          console.log('Error response', err);
        }
      );
    };
    $scope.openEBDState = function (vessel) {
      $window.localStorage.removeItem('libShipId');
      $window.localStorage.removeItem('libshipName');
      $window.localStorage.setItem('libShipId', vessel.id);
      $window.localStorage.setItem('libshipName', vessel.vesselsName);
      $state.go('dapp.userVesselDocumentEBD');
    };
    $scope.checkAll = function () {
      console.log('inside the check all function');
      if ($scope.selectedAll) {
        $scope.selectedAll = true;
      } else {
        $scope.selectedAll = false;
      }
      console.log('value:::' + $scope.selectedAll);
      $scope.subCheckBox = $scope.selectedAll;
    };
    $scope.deleteEntityNotification = function (id) {
      console.log('inside the entity changes method:::' + id);
      var notificationId = { notificationId: id };
      console.log('before check' + $scope.deleteNotificationList.length);
      $scope.deleteNotificationList.push(notificationId);
      console.log(
        'ready for delete notification:::' +
          JSON.stringify($scope.deleteNotificationList)
      );
    };
    $scope.deleteAllNotification = function (list) {
      console.log(
        'click deleteAll check' + $scope.deleteNotificationList.length
      );
      if ($scope.deleteNotificationList.length == 0)
        $scope.deleteNotificationList = list;
    };
    $scope.deleteAllNotificationBySelect = function () {
      var userDto = { userId: $window.localStorage.getItem('userId') };
      FunctionalityService.deleteAllNotification(userDto).then(
        function mySuccess(response) {
          $('#deleteAll').modal('hide');
          $state.reload();
          $timeout(function () {
            toaster.success({ title: response.data.status });
          }, 1000);
        },
        function myError(err) {
          toaster.error({ title: myError.data.status });
          console.log('Error response');
        }
      );
    };
    $scope.deleteNotification = function (id) {
      console.log('inside delete notification:::::' + id);
      $scope.notificationId = id;
    };
    $scope.deleteNotificationById = function () {
      var notificationId = { notificationId: $scope.notificationId };
      console.log('inside delete notification:::::' + $scope.notificationId);
      FunctionalityService.deleteNotification(notificationId).then(
        function mySuccess(response) {
          $('#delete').modal('hide');
          $state.reload();
          $timeout(function () {
            toaster.success({ title: response.data.status });
          }, 1000);
        },
        function myError(err) {
          toaster.error({ title: myError.data.status });
          console.log('Error response');
        }
      );
    };
  },
]);
