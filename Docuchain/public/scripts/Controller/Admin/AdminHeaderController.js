var adminDashboard = angular.module('dapp.AdminHeaderController', ['$idle']);

adminDashboard.controller('AdminHeaderController', [
  '$scope',
  '$window',
  '$location',
  '$state',
  '$rootScope',
  'FunctionalityService',
  '$idle',
  'toaster',
  function (
    $scope,
    $window,
    $location,
    $state,
    $rootScope,
    FunctionalityService,
    $idle,
    toaster
  ) {
    $scope.userProfileId = $window.localStorage.getItem('userId');
    $scope.logoPath = $window.localStorage.getItem('logoPicture');
    var sessionData = $window.localStorage.getItem('userName');
    $scope.loader = false;

    $scope.sidebarActiveFun = function () {
      if ($rootScope.sidebarActive == true) {
        $rootScope.sidebarActive = false;
      } else {
        $rootScope.sidebarActive = true;
      }
    };

    //This method is used for idle session timeout
    $scope.$on('$userTimeout', function () {
      $state.go('session');
    });
    $scope.$on('$viewContentLoaded', function () {
      var data = { userId: $scope.userProfileId };
      FunctionalityService.getDocumentNotification(data).then(
        function mySuccess(response) {
          console.info('getDocumentNOtification', response);
          $scope.notificationList = response.data.userList;
        },
        function myError(err) {
          console.log('Error response');
        }
      );
    });
    // var sessionData = JSON.parse(sessionStorage.getItem("names"));
    $scope.$on('$viewContentLoaded', function () {
      var data = { userId: $scope.userProfileId };
      FunctionalityService.getDocumentNotificationCount(
        JSON.stringify(data)
      ).then(
        function mySuccess(response) {
          $scope.notificationCountList = response.data.userList;
        },
        function myError(err) {
          console.log('Error response');
        }
      );
    });

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
    $scope.$on('$viewContentLoaded', function () {
      //var userId = $scope.sessionUserId;
      FunctionalityService.getDashboardTopCount($scope.userProfileId).then(
        function mySuccess(response) {
          if (response.data != null) {
            $scope.dashboardTopCount = response.data.shipProfileList;
          }
        },
        function myError(err) {
          console.log('Error response');
        }
      );
    });
    $scope.$on('$viewContentLoaded', function () {
      FunctionalityService.getOrganizationUserList($scope.userProfileId).then(
        function mySuccess(response) {
          if (response.status == 201 || response.status == 200) {
            $scope.adminUserList = JSON.stringify(response.data.getUserList);
            $scope.adminUserList = response.data.getUserList;
            if ($scope.adminUserList == undefined) {
              // toaster.clear();
              // toaster.info({ title: "No records found" });
            }
          }
        },
        function myError(err) {
          $scope.loader = false;
          console.log('Error response', err);
        }
      );
    });
    $scope.$on('$viewContentLoaded', function () {
      var data = { userId: $scope.userProfileId };
      FunctionalityService.getDocumentNotification(data).then(
        function mySuccess(response) {
          console.info('getDocumentNotification', response);
          $scope.notificationList = response.data.userList;
        },
        function myError(err) {
          $scope.loader = false;
          console.log('Error response', err);
        }
      );
    });
    $scope.notification = function () {
      $scope.loader = true;
      var data = { userId: $scope.userProfileId };
      //$state.go('dapp.notification');
      FunctionalityService.setDocumentNotificationViewed(data).then(
        function mySuccess(response) {
          $scope.loader = false;
          //$window.location.reload();
          $state.go('dapp.notificationadmin');
        },
        function myError(err) {
          $scope.loader = false;
          console.log('Error response', err);
        }
      );
    };

    $scope.userDashboard = function () {
      console.log('inside the user dashboard');
      $state.go('dapp.adminUsers');
    };

    $scope.vesselsInformation = function () {
      console.log('inside the user dashboard vessels');
      $state.go('dapp.adminVessels');
    };
    if (sessionData == null || sessionData == undefined) {
      $location.path('/');
    } else if (sessionData != null) {
      $scope.storedData = sessionData.loginInfo;
    }

    // listen to storage event
    $scope.logout = function () {
      $window.localStorage.removeItem('sessionObject');
      $window.localStorage.removeItem('userRole');
      $window.localStorage.removeItem('userName');
      $window.localStorage.removeItem('userEmail');
      $window.localStorage.removeItem('userId');
      $window.localStorage.removeItem('roleId');
      $window.localStorage.removeItem('role');
      $window.localStorage.removeItem('organizationId');
      $window.localStorage.removeItem('organizationName');
      $window.localStorage.removeItem('profilePicture');
      $window.localStorage.removeItem('maxShipCount');
      $window.localStorage.removeItem('maxUserCount');
      $window.localStorage.removeItem('shipProfileInfos');
      $window.localStorage.removeItem('groupShipId');
      $window.localStorage.removeItem('groupShipName');
      $window.localStorage.removeItem('editId');
      $window.localStorage.removeItem('countryName');
      $window.localStorage.removeItem('stateName');
      $window.localStorage.removeItem('shipId');
      $window.localStorage.removeItem('libShipId');
      $window.localStorage.removeItem('libshipName');
      $window.localStorage.removeItem('logoPicture');
      localStorage.removeItem('logout-event');

      toaster.pop('success', 'Logout succesfully');
      setTimeout(function () {
        $location.path('/');
      }, 2000);
      $window.location.reload();
    };

    $(document).ready(function () {
      $('#logout').on('click', function () {
        // change logout-event and therefore send an event
        localStorage.setItem('logout-event', 'logout' + Math.random());
        return true;
      });
    });

    window.addEventListener(
      'storage',
      function (event) {
        if (event.key == 'logout-event') {
          $window.localStorage.removeItem('sessionObject');
          $window.localStorage.removeItem('userRole');
          $window.localStorage.removeItem('userName');
          $window.localStorage.removeItem('userEmail');
          $window.localStorage.removeItem('userId');
          $window.localStorage.removeItem('roleId');
          $window.localStorage.removeItem('role');
          $window.localStorage.removeItem('organizationId');
          $window.localStorage.removeItem('organizationName');
          $window.localStorage.removeItem('profilePicture');
          $window.localStorage.removeItem('maxShipCount');
          $window.localStorage.removeItem('maxUserCount');
          $window.localStorage.removeItem('shipProfileInfos');
          $window.localStorage.removeItem('groupShipId');
          $window.localStorage.removeItem('groupShipName');
          $window.localStorage.removeItem('editId');
          $window.localStorage.removeItem('countryName');
          $window.localStorage.removeItem('stateName');
          $window.localStorage.removeItem('shipId');
          $window.localStorage.removeItem('libShipId');
          $window.localStorage.removeItem('libshipName');
          localStorage.removeItem('logout-event');
          $window.localStorage.removeItem('logoPicture');

          toaster.pop('success', 'Logout succesfully');
          setTimeout(function () {
            $location.path('/');
          }, 2000);
          $window.location.reload();
        } else if (event.key != 'logout-event') {
          if (localStorage.roleId == '2') {
            // $state.go('dapp.admindashboard');
          }
          if (localStorage.roleId == '1') {
            // $state.go('dapp.saDashboard');
          } else if (
            localStorage.roleId == '3' ||
            localStorage.roleId == '4' ||
            localStorage.roleId == '5' ||
            localStorage.roleId == '6'
          ) {
            //$state.go('dapp.userDashboard');
          }
        }
      },
      false
    );
  },
]);
