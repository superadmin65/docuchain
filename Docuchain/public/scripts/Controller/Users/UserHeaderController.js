var userHeader = angular.module('dapp.UserHeaderController',['$idle']);

userHeader.controller('UserHeaderController',['$scope','$window', '$location','$state', '$rootScope','FunctionalityService','$idle', 'toaster' ,function($scope, $window, $location,$state, $rootScope, FunctionalityService,$idle, toaster){

    $scope.userProfileId = $window.localStorage.getItem('userId');
    var sessionData = $window.localStorage.getItem("userName");
    $scope.logoPath = $window.localStorage.getItem('logoPicture');
    $scope.headerDetails;
    
    $scope.sidebarActiveFun=function(){
        if($rootScope.sidebarActive==true){
            $rootScope.sidebarActive=false;
        }
        else{
            $rootScope.sidebarActive=true;
        }
    }
    $scope.$on('$userTimeout', function() {
      $state.go('session');
    });

    $scope.$on('$viewContentLoaded', function () {
      var data = { "userId": $scope.userProfileId,
                    "filterByDay" : "Lastweek"};
        FunctionalityService.getDocumentNotification(data)
          .then(function mySuccess(response) {
            $scope.notificationList = response.data.userList;
            console.log("$scope.notificationList:::",JSON.stringify($scope.notificationList));
          }, function myError(err) {
            console.log("Error response");
          });
   
      });

      $scope.statusChange =  function(changestatus){
        //console.log("sgtats:",changestatus)
        $rootScope.statusForFilter = changestatus;
        //$state.go('dapp.userVesselDocumentEBD');
        $state.reload();

      }
      //var sessionData = JSON.parse(sessionStorage.getItem("names"));
      $scope.$on('$viewContentLoaded', function () {   
        var data = { "userId": $scope.userProfileId }; 
        FunctionalityService.getDocumentNotificationCount(data)
          .then(function mySuccess(response) {
            $scope.notificationCountList = response.data.userList;
          }, function myError(err) {
            console.log("Error response");
          });
  
      });
    $scope.sessionObject=JSON.parse($window.localStorage.getItem('sessionObject'));

    $scope.contentHeaderLoad=function(){
        var userId=$scope.sessionObject.userId;
        FunctionalityService.getDashboardTopCount(userId) 
        .then(function mySuccess(response) {
          if (response.status==200) {
           $scope.dashboardTopCount = response.data.shipProfileList;
           $rootScope.headerDetails = $scope.dashboardTopCount[0];
           console.log("headerDetails::"+JSON.stringify($rootScope.headerDetails));         
          }
        }, function myError(err) {
          console.log("Error response");
        });
    }

    $scope.notification = function (){
        var data = { "userId":$scope.userProfileId};
         $state.go('dapp.userNotification');
        FunctionalityService.setDocumentNotificationViewed(data)
          .then(function mySuccess(response) {
            //$window.location.reload();
            $state.go('dapp.userNotification');
          }, function myError(err) {
            console.log("Error response");
          });
      }

      $scope.faq = function (){      
            $state.go('dapp.faq');        
      }
      if (sessionData == null || sessionData == undefined) {
        $location.path('/login');
      } else if (sessionData != null) {
        $scope.storedData = sessionData.loginInfo
      }
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
                 // $state.go('dapp.admindashboard');
    
              }
              if (localStorage.roleId == "1") {
                  //$state.go('dapp.saDashboard');
    
              }
              else if (localStorage.roleId == "3" || localStorage.roleId == "4" || localStorage.roleId == "5" || localStorage.roleId == "6") {
                 // $state.go('dapp.userDashboard');
              }
          }
          }, false);

      $scope.vesselsInformation = function () {
            if ($window.localStorage.getItem('roleId') == 3){
              $rootScope.selected = 1; 
              $state.go('dapp.userVesselDocumentEBD');
              
            }                        
            else {
              $rootScope.selected = 1; 
              $state.go('dapp.userVesselDocument');
            }
            
          }
  

}]);