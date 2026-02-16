var saHeader = angular.module('dapp.SaHeaderController',['$idle']);

saHeader.controller('SaHeaderController',['$scope','$window', '$location','$state', '$rootScope','FunctionalityService','$idle', 'toaster', function($scope, $window, $location,$state, $rootScope, FunctionalityService,$idle, toaster){
    var sessionData = $window.localStorage.getItem("userName");

    $scope.sidebarActiveFun=function(){
        if($rootScope.sidebarActive==true){
            $rootScope.sidebarActive=false;
        }
        else{
            $rootScope.sidebarActive=true;
        }
    }

    //This method is used for idle session timeout
    $scope.$on('$userTimeout', function() {
        $state.go('session');
      });
      if (sessionData == null || sessionData == undefined) {
        $location.path('/');
      } else if (sessionData != null) {
        $scope.storedData = sessionData.loginInfo
      }
      $scope.$on('$viewContentLoaded', function () {  
        FunctionalityService.getOrganizationTopCount()
          .then(function (response) { 
            if (response.status == 200) {            
              $scope.organization = response.data.organizationInfo;
            }
          }, function (error) {
            console.log("message :: " + error);
          });
      })

   // listen to storage event

   $scope.logout = function(){
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
    $window.localStorage.removeItem('groupShipId')
    $window.localStorage.removeItem('groupShipName');
    $window.localStorage.removeItem('editId');
    $window.localStorage.removeItem('countryName');
    $window.localStorage.removeItem('stateName');
    $window.localStorage.removeItem('shipId');
    $window.localStorage.removeItem('libShipId');
    $window.localStorage.removeItem('libshipName');
    localStorage.removeItem('logout-event');
    $window.localStorage.removeItem('logoPicture');
    toaster.pop("success", "Logout succesfully");
    setTimeout(function () {
      $location.path('/');
    }, 2000)
    $window.location.reload();
   }
   $(document).ready(function () {

    $('#logout').on('click', function () {
      // change logout-event and therefore send an event
      localStorage.setItem('logout-event', 'logout' + Math.random());
      return true;
    });

  });

  window.addEventListener('storage', function (event) {
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
          $window.localStorage.removeItem('groupShipId')
          $window.localStorage.removeItem('groupShipName');
          $window.localStorage.removeItem('editId');
          $window.localStorage.removeItem('countryName');
          $window.localStorage.removeItem('stateName');
          $window.localStorage.removeItem('shipId');
          $window.localStorage.removeItem('libShipId');
          $window.localStorage.removeItem('libshipName');
          localStorage.removeItem('logout-event');
          $window.localStorage.removeItem('logoPicture');
          toaster.pop("success", "Logout succesfully");
          setTimeout(function () {
            $location.path('/');
          }, 2000)
          $window.location.reload();
        }

        else if (event.key != 'logout-event') {
                
          if (localStorage.roleId == "2") {
              $state.go('dapp.admindashboard');

          }
          if (localStorage.roleId == "1") {
              $state.go('dapp.saDashboard');

          }
          else if (localStorage.roleId == "3" || localStorage.roleId == "4" || localStorage.roleId == "5" || localStorage.roleId == "6") {
              $state.go('dapp.userDashboard');
          }
      }
      }, false);


}]);