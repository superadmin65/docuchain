var saDashboard = angular.module('dapp.SaDashboardController',['$idle','angularUtils.directives.dirPagination']);

saDashboard.controller('SaDashboardController',['$scope','$window', '$location','$state', '$rootScope','FunctionalityService','$idle',function($scope, $window, $location,$state, $rootScope, FunctionalityService,$idle){
    
    $scope.currentPage = 1;
    $scope.viewby = 10;
    $scope.itemsPerPage = $scope.viewby;


    $scope.$on('$viewContentLoaded', function () {  
        FunctionalityService.getStatisticsDetail()
          .then(function (response) {  
            if (response.status == 200) {            
              $scope.organizationdetaillist = response.data.organizationInfos;
            }
          }, function myError(err) {
            $scope.loader = false;
            console.log("Error response", err);
          }); 
      })

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
}]);