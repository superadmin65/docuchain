// var adminDashboard = angular.module('dapp.AdminDashboardController', [
//   'angularUtils.directives.dirPagination',
// ]);
// adminDashboard.directive('fileInput', [
//   '$parse',
//   function ($parse) {
//     return {
//       restrict: 'A',
//       link: function (scope, ele, attrs) {
//         ele.bind('change', function () {
//           $parse(attrs.fileInput).assign(scope, ele[0].files);
//           scope.$apply();
//         });
//       },
//     };
//   },
// ]);
// adminDashboard.controller('AdminDashboardController', [
//   '$scope',
//   '$window',
//   '$location',
//   '$state',
//   '$rootScope',
//   'FunctionalityService',
//   // 'uiGmapGoogleMapApi',
//   // 'uiGmapIsReady',
//   'toaster',
//   '$timeout',
//   function (
//     $scope,
//     $window,
//     $location,
//     $state,
//     $rootScope,
//     FunctionalityService,
//     // uiGmapGoogleMapApi,
//     // uiGmapIsReady,
//     toaster,
//     $timeout
//   ) {
//     if (localStorage.length == 0) {
//       $location.path('/');
//     }

//     $scope.sessionObject = JSON.parse(
//       $window.localStorage.getItem('sessionObject')
//     );
//     $scope.userProfileId = $window.localStorage.getItem('userId');
//     $scope.profilePictures = $window.localStorage.getItem('profilePicture');

//     $scope.currentPage = 1;
//     $scope.viewby = 5;
//     $scope.itemsPerPage = $scope.viewby;
//     $scope.shipProfileList = [];
//     $scope.editShow = true;
//     $scope.resetShow = false;
//     $scope.approveArray = [];
//     $scope.rejectedArray = [];
//     $rootScope.pendingArray = [];
//     $rootScope.geoLocationlist = [];
//     $rootScope.markers = [];
//     $scope.profilePicture;
//     $scope.loader = false;
//     var latitudehome = 1.3521;
//     var longitutehome = 103.8198;

//     $rootScope.active = $scope.sessionObject.subscriptionExpireStatus;

//     $scope.thumbnail = {
//       dataUrl: '',
//     };
//     if ($scope.profilePictures != 'undefined') {
//       $scope.thumbnail.dataUrl = $scope.profilePictures;
//     } else {
//       $scope.thumbnail = {
//         dataUrl: 'undefined',
//       };
//     }
//     $scope.fileReaderSupported = window.FileReader != null;
//     $scope.uploadFiledp = function (files) {
//       if (files != null) {
//         var file = files[0];
//         if (files[0].size > 2048000) {
//           $rootScope.errorFile = document.getElementById(
//             'sizeOffile'
//           ).innerHTML = 'Picture should be below 1MB';
//         } else {
//           document.getElementById('sizeOffile').innerHTML = '';
//           $rootScope.errorFile = '';
//         }
//         $scope.profilePicture = file;
//         if ($scope.fileReaderSupported && file.type.indexOf('image') > -1) {
//           $timeout(function () {
//             var fileReader = new FileReader();
//             fileReader.readAsDataURL(file);
//             fileReader.onload = function (e) {
//               $timeout(function () {
//                 $scope.thumbnail.dataUrl = e.target.result;
//                 $scope.profilePictures = $scope.thumbnail.dataUrl;
//               });
//             };
//           });
//         }
//       }
//     };

//     $scope.userDetails = function () {
//       $state.go('dapp.adminProfile');
//       $scope.editShow = true;
//       $scope.resetShow = false;
//     };

//     $scope.editProfile = function () {
//       $scope.editShow = true;
//       $scope.resetShow = false;
//       $state.reload();
//     };

//     $scope.getPendingCount = function () {
//       $scope.loader = true;

//       FunctionalityService.getPendingRequest().then(
//         function (response) {
//           $scope.loader = false;

//           if (response.status == 200) {
//             $scope.pendingList = response.data.requestUserDTOs;
//             angular.forEach($scope.pendingList, function (value) {
//               if (value.requestUserStatus == 'Approved') {
//                 $scope.approveArray.push(value);
//               } else if (value.requestUserStatus == 'Rejected') {
//                 $scope.rejectedArray.push(value);
//               } else if (value.requestUserStatus == 'Pending') {
//                 $rootScope.pendingArray.push(value);
//               }
//             });
//           }
//         },
//         function myError(err) {
//           $scope.loader = false;
//           console.log('Error response', err);
//         }
//       );

//       FunctionalityService.getTaskAssignedToUser($scope.userId).then(
//         function (response) {
//           $scope.loader = false;
//           if (response.status == 200 || response.status == 201) {
//             $scope.message = JSON.stringify(response.data.taskAssignedToUser);

//             $scope.taskAssignedToUser = response.data.taskAssignedToUser;
//             $scope.taskAssignedToUsernew = [];
//             angular.forEach($scope.taskAssignedToUser, function (value) {
//               if (
//                 value.taskStatus != 'Completed' &&
//                 value.taskStatus != 'Rejected'
//               ) {
//                 $scope.taskAssignedToUsernew.push(value);
//               }
//             });
//             console.log(
//               ' $scope.taskAssignedToUser0' + $scope.taskAssignedToUser
//             );
//           } else {
//           }
//         },
//         function myError(err) {
//           $scope.loader = false;
//           console.log('Error response', err);
//         }
//       );
//     };

//     $scope.$on('$viewContentLoaded', function () {
//       console.log('inside the geolocation onbload');
//       FunctionalityService.geoLocationlist($scope.sessionObject.userId).then(
//         function (response) {
//           if (response.status == 200) {
//             console.log('geoLocationlist Response', response);
//             $rootScope.geoLocationlist = response.data.userList;
//             console.log(
//               'geoLocationlist userList Response',
//               $scope.geoLocationlist
//             );
//           }
//         },
//         function myError(err) {
//           $scope.loader = false;
//           console.log('Error response', err);
//         }
//       );
//     });

//     // $scope.map = {center: {latitude: 51.219053, longitude: 4.404418 }, zoom: 14 };
//     // uiGmapGoogleMapApi.then(function (maps) {
//     //   //var dogParks = $meteor.collection(DogParks);
//     //   // var dogParks = genarateDogParks(2000);
//     //   console.log(
//     //     'inside the map locationlength::' +
//     //       JSON.stringify($scope.geoLocationlist)
//     //   );
//     //   var dogParks = genarateDogParks($rootScope.geoLocationlist.length);

//     //   function assignMarkersToMap() {
//     //     angular.forEach($rootScope.geoLocationlist, function (item) {
//     //       if (
//     //         item.geoLocationInfos.latitude != undefined &&
//     //         item.geoLocationInfos.longitute != undefined
//     //       ) {
//     //         latitudehome = item.geoLocationInfos.latitude;
//     //         longitutehome = item.geoLocationInfos.longitute;
//     //         $rootScope.markers.push({
//     //           latitude: item.geoLocationInfos.latitude,
//     //           longitude: item.geoLocationInfos.longitute,
//     //           title: item.shipProfileDTO.shipName,
//     //           id: item.geoLocationInfos.id,
//     //           updateDate: item.geoLocationInfos.updateDate,
//     //           imomap: item.shipProfileDTO.imo,
//     //           icon: 'image/shipIcon.png',
//     //         });
//     //       }
//     //     });
//     //     console.log('default values::', latitudehome + '...' + longitutehome);

//     //     //console.log("inside assignMarkersToMap::" + JSON.stringify($rootScope.markers));
//     //     return $rootScope.markers;
//     //   }

//     //   $scope.map = {
//     //     zoom: 4,
//     //     bounds: {},
//     //     center: {
//     //       latitude: latitudehome,
//     //       longitude: longitutehome,
//     //     },
//     //   };

//     //   $scope.markers = [];

//     //   // uiGmapIsReady.promise().then(function (instances) {
//     //   //   $scope.markers = assignMarkersToMap();
//     //   //   console.log(
//     //   //     'inside assignMarkersToMap222::' + JSON.stringify($scope.markers)
//     //   //   );
//     //   // });

//     //   $scope.windowCoords = {};

//     //   $scope.onClick = function (marker, eventName, model) {
//     //     console.log('inside the onClick function' + JSON.stringify(model));
//     //     $scope.map.center.latitude = model.latitude;
//     //     $scope.map.center.longitude = model.longitude;
//     //     $scope.map.zoom = 11;
//     //     $scope.windowCoords.latitude = model.latitude;
//     //     $scope.windowCoords.longitude = model.longitude;
//     //     $scope.longitude = $scope.windowCoords.longitude;
//     //     $scope.latitude = $scope.windowCoords.latitude;
//     //     $scope.parkName = model.title;
//     //     $scope.updateDate = model.updateDate;
//     //     $scope.imo = model.imomap;

//     //     $scope.show = true;
//     //   };

//     //   $scope.closeClick = function () {
//     //     $scope.show = false;
//     //   };

//     //   $scope.options = {
//     //     scrollwheel: false,
//     //   };

//     //   $scope.show = false;
//     // });

//     $scope.editProfileSubmit = function (data) {
//       $scope.loader = true;

//       toaster.clear();

//       if ($rootScope.errorFile === 'Picture should be below 1MB') {
//         $scope.loader = false;

//         toaster.pop('error', $rootScope.errorFile);
//       } else {
//         $rootScope.errorFile = '';
//         $scope.editShow = true;
//         var data = {
//           userId: $scope.userProfileId,
//           firstName: data.firstName,
//           lastName: data.lastName,
//           mail: data.mail,
//         };

//         FunctionalityService.editProfileData(data, $scope.profilePicture).then(
//           function (response) {
//             $scope.loader = false;

//             if (response.status == 200 || response.status == 201) {
//               console.log('editProfileData', response);
//               toaster.pop('success', response.data.message);
//               $scope.message = response.data.userInfos;

//               $window.localStorage.setItem(
//                 'sessionObject',
//                 JSON.stringify($scope.message)
//               );
//               $window.localStorage.setItem(
//                 'userRole',
//                 $scope.message.businessCategory
//               );
//               $window.localStorage.setItem('userName', $scope.message.userName);
//               $window.localStorage.setItem('userEmail', $scope.message.mail);
//               $window.localStorage.setItem('userId', $scope.message.userId);
//               $window.localStorage.setItem('roleId', $scope.message.roleId);
//               $window.localStorage.setItem('role', $scope.message.role);
//               $window.localStorage.setItem(
//                 'organizationId',
//                 $scope.message.organizationId
//               );
//               $window.localStorage.setItem(
//                 'profilePicture',
//                 $scope.message.profilePicture
//               );

//               $scope.resetShow = false;
//               $scope.editShow = true;
//               setTimeout(function () {
//                 $window.location.reload();
//               }, 1000);
//             } else {
//               toaster.pop('error', response.data.message);
//               $scope.editShow = true;
//               $scope.resetShow = false;
//             }
//           },
//           function myError(err) {
//             $scope.loader = false;
//             console.log('Error response', err);
//           }
//         );
//       }
//     };
//     $scope.resetPassword = function () {
//       $scope.resetShow = true;
//       $scope.editShow = false;
//     };
//     $scope.resetPasswordSubmit = function (data) {
//       $scope.loader = true;

//       toaster.clear();

//       var data = {
//         userId: $scope.userProfileId,
//         currentPassword: data.currentPassword,
//         password: data.password,
//         confirmPassword: data.confirmPassword,
//       };

//       FunctionalityService.resetPswd(data).then(
//         function (response) {
//           $scope.loader = false;

//           if (response.status == 200 || response.status == 201) {
//             toaster.pop('success', response.data.message);
//             $scope.resetShow = false;
//             $scope.editShow = true;
//             setTimeout(function () {
//               $state.reload();
//             }, 1000);
//           } else {
//             toaster.pop('error', response.data.message);
//             $scope.resetShow = true;
//             $scope.editShow = false;
//           }
//         },
//         function myError(err) {
//           $scope.loader = false;
//           console.log('Error response', err);
//         }
//       );
//     };

//     $scope.sidebarActiveFun = function () {
//       if ($rootScope.sidebarActive == true) {
//         $rootScope.sidebarActive = false;
//       } else {
//         $rootScope.sidebarActive = true;
//       }
//     };
//     $scope.getAllVesselListAndCount = function () {
//       $scope.loader = true;

//       FunctionalityService.getViewShipProfile($scope.userProfileId).then(
//         function mySuccess(response) {
//           $scope.loader = false;
//           if (response.data != null) {
//             $scope.shipProfileList = response.data.shipProfileList;
//           }
//         },
//         function myError(err) {
//           $scope.loader = false;
//           console.log('Error response', err);
//         }
//       );
//     };

//     $scope.expandConfigSubMenu = function () {
//       if ($rootScope.subConfigMenuActive == true) {
//         $rootScope.subConfigMenuActive = false;
//       } else {
//         $rootScope.subConfigMenuActive = true;
//       }
//     };

//     $scope.adminReportIssueClick = function () {
//       $rootScope.subConfigMenuActive = false;
//       $rootScope.subConfigMenuActive = false;
//     };

//     $scope.admindashboardClick = function () {
//       $rootScope.subConfigMenuActive = false;
//     };

//     $scope.adminVesselsClick = function () {
//       $rootScope.subConfigMenuActive = false;
//     };

//     $scope.adminUsersClick = function () {
//       $rootScope.subConfigMenuActive = false;
//     };

//     $scope.adminUserExtensionClick = function () {
//       $rootScope.subConfigMenuActive = false;
//     };

//     $scope.adminTasksClick = function () {
//       $rootScope.subConfigMenuActive = false;
//     };

//     $scope.adminsuperAauditLogClick = function () {
//       $rootScope.subConfigMenuActive = false;
//     };

//     function genarateDogParks(count) {
//       var vals = [];
//       for (var i = 0; i < count; i++) {
//         vals.push({
//           latitude: $scope.geoLocationlist[i].geoLocationInfos.latitude,
//           longitude: $scope.geoLocationlist[i].geoLocationInfos.longitute,
//           _id: i,
//           // name: 'Dog Park #' + i
//           name: $scope.geoLocationlist[i].shipProfileDTO.shipName,
//           imo: $scope.geoLocationlist[i].shipProfileDTO.imo,
//           updateDate: $scope.geoLocationlist[i].geoLocationInfos.updateDate,
//         });
//       }
//       console.log('inside the genarateDogParks::' + JSON.stringify(vals));
//       return vals;
//     }

//     function getRandomArbitrary(min, max) {
//       return Math.random() * (max - min) + min;
//     }
//     $scope.routrReload = function () {
//       $state.reload();
//     };
//   },
// ]);

var adminDashboard = angular.module('dapp.AdminDashboardController', [
  'angularUtils.directives.dirPagination',
]);

adminDashboard.directive('fileInput', [
  '$parse',
  function ($parse) {
    return {
      restrict: 'A',
      link: function (scope, ele, attrs) {
        ele.bind('change', function () {
          $parse(attrs.fileInput).assign(scope, ele[0].files);
          scope.$apply();
        });
      },
    };
  },
]);

adminDashboard.controller('AdminDashboardController', [
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
    // Basic length check
    if (localStorage.length == 0) {
      $location.path('/');
      return;
    }

    // ✅ FIX 1: STOP CONTROLLER CRASH
    $scope.sessionObject = JSON.parse(
      $window.localStorage.getItem('sessionObject')
    );

    // 🚨 SAFETY CHECK
    if (!$scope.sessionObject) {
      console.log('Session not found. Redirecting to login.');
      $location.path('/');
      return; // Stop execution before we try to access properties of null
    }

    $scope.userProfileId = $window.localStorage.getItem('userId');
    $scope.profilePictures = $window.localStorage.getItem('profilePicture');

    $scope.currentPage = 1;
    $scope.viewby = 5;
    $scope.itemsPerPage = $scope.viewby;
    $scope.shipProfileList = [];
    $scope.editShow = true;
    $scope.resetShow = false;
    $scope.approveArray = [];
    $scope.rejectedArray = [];
    $rootScope.pendingArray = [];
    $rootScope.geoLocationlist = [];
    $rootScope.markers = [];
    $scope.loader = false;

    // ✅ FIX 1 (Continued): Safe assignment
    $rootScope.active = $scope.sessionObject.subscriptionExpireStatus || null;

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

    $scope.fileReaderSupported = window.FileReader != null;
    $scope.uploadFiledp = function (files) {
      if (files != null) {
        var file = files[0];
        if (files[0].size > 2048000) {
          $rootScope.errorFile = 'Picture should be below 1MB';
          // Using a safer way to update the DOM if needed, though toaster is better
          var sizeEl = document.getElementById('sizeOffile');
          if (sizeEl) sizeEl.innerHTML = $rootScope.errorFile;
        } else {
          var sizeEl = document.getElementById('sizeOffile');
          if (sizeEl) sizeEl.innerHTML = '';
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

    $scope.userDetails = function () {
      $state.go('dapp.adminProfile');
      $scope.editShow = true;
      $scope.resetShow = false;
    };

    $scope.getPendingCount = function () {
      $scope.loader = true;

      FunctionalityService.getPendingRequest().then(
        function (response) {
          $scope.loader = false;
          if (response.status == 200) {
            $scope.pendingList = response.data.requestUserDTOs;
            angular.forEach($scope.pendingList, function (value) {
              if (value.requestUserStatus == 'Approved') {
                $scope.approveArray.push(value);
              } else if (value.requestUserStatus == 'Rejected') {
                $scope.rejectedArray.push(value);
              } else if (value.requestUserStatus == 'Pending') {
                $rootScope.pendingArray.push(value);
              }
            });
          }
        },
        function myError(err) {
          $scope.loader = false;
          console.log('Error response', err);
        }
      );

      // ✅ FIX 2: Correct variable name (userId -> userProfileId)
      FunctionalityService.getTaskAssignedToUser($scope.userProfileId).then(
        function (response) {
          $scope.loader = false;
          if (response.status == 200 || response.status == 201) {
            $scope.taskAssignedToUser = response.data.taskAssignedToUser;
            $scope.taskAssignedToUsernew = [];
            angular.forEach($scope.taskAssignedToUser, function (value) {
              if (
                value.taskStatus != 'Completed' &&
                value.taskStatus != 'Rejected'
              ) {
                $scope.taskAssignedToUsernew.push(value);
              }
            });
          }
        },
        function myError(err) {
          $scope.loader = false;
          console.log('Error response', err);
        }
      );
    };

    $scope.$on('$viewContentLoaded', function () {
      console.log('inside the geolocation onload');
      // ✅ FIX 3: Geolocation call safety check
      if ($scope.sessionObject && $scope.sessionObject.userId) {
        FunctionalityService.geoLocationlist($scope.sessionObject.userId).then(
          function (response) {
            if (response.status == 200) {
              $rootScope.geoLocationlist = response.data.userList;
            }
          },
          function myError(err) {
            $scope.loader = false;
            console.log('Error response', err);
          }
        );
      }
    });

    $scope.editProfileSubmit = function (data) {
      $scope.loader = true;
      toaster.clear();

      if ($rootScope.errorFile === 'Picture should be below 1MB') {
        $scope.loader = false;
        toaster.pop('error', $rootScope.errorFile);
      } else {
        $rootScope.errorFile = '';
        var updateData = {
          userId: $scope.userProfileId,
          firstName: data.firstName,
          lastName: data.lastName,
          mail: data.mail,
        };

        FunctionalityService.editProfileData(
          updateData,
          $scope.profilePicture
        ).then(
          function (response) {
            $scope.loader = false;
            if (response.status == 200 || response.status == 201) {
              toaster.pop('success', response.data.message);
              $scope.message = response.data.userInfos;

              // Syncing storage with new data
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

              setTimeout(function () {
                $window.location.reload();
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
      }
    };

    $scope.resetPasswordSubmit = function (data) {
      $scope.loader = true;
      toaster.clear();
      var resetData = {
        userId: $scope.userProfileId,
        currentPassword: data.currentPassword,
        password: data.password,
        confirmPassword: data.confirmPassword,
      };

      FunctionalityService.resetPswd(resetData).then(
        function (response) {
          $scope.loader = false;
          if (response.status == 200 || response.status == 201) {
            toaster.pop('success', response.data.message);
            setTimeout(function () {
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

    $scope.routrReload = function () {
      $state.reload();
    };
  },
]);
