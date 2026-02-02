var saCountry = angular.module('dapp.SaConfigCountryController', ['angularUtils.directives.dirPagination','toaster']);

saCountry.controller('SaConfigCountryController', ['$scope', '$window', '$location', '$state', '$rootScope','$timeout','toaster', 'FunctionalityService', function ($scope, $window, $location, $state, $rootScope,$timeout,toaster, FunctionalityService) {
    $scope.currentPage = 1;
    $scope.viewby = 10;
    $scope.itemsPerPage = $scope.viewby;
    $scope.userProfileId = $window.localStorage.getItem('userId');

    $scope.loader= false;

    $scope.expandSubMenu = function () {
        if ($rootScope.subMenuActive == true) {
            $rootScope.subMenuActive = false;
        }
        else {
            $rootScope.subMenuActive = true;
            $rootScope.subConfigMenuActive = false;
        }

    }

    $scope.expandConfigSubMenu = function () {
        if ($rootScope.subConfigMenuActive == true) {
            $rootScope.subConfigMenuActive = false;
        }
        else {
            $rootScope.subConfigMenuActive = true;
            $rootScope.subMenuActive = false;
        }

    }

    $scope.countryList = function () {
        $scope.loader= true;

        FunctionalityService.getCountryList($scope.userProfileId)
            .then(function mySuccess(response) {
                $scope.loader= false;

                if (response.status == 201 || response.status == 200) {

                    $scope.allCountryList = JSON.stringify(response.data.countryInfos);
                    $scope.allCountryList = response.data.countryInfos;
                    if ($scope.allCountryList == undefined) {
                        toaster.clear();
                        toaster.info({ title: "No records found" });
                    }
                }
            }, function myError(err) {
                $scope.loader = false;
                console.log("Error response", err);
              });

    }
   
    
    $scope.addCountry = function () {
        $scope.loader= true;

        var addcountrydata = { "adminId": $scope.userProfileId, "countryName": $scope.country.countryName, "countryCode": $scope.country.countryCode };
        FunctionalityService.addCountry(addcountrydata)
            .then(function mySuccess(response) {
                $scope.loader= false;

                if (response.status == 201 || response.status == 200) {
                    $('#countryadd').modal('hide');
                    $state.reload();
                    $timeout(function () {
                       toaster.clear();
                        toaster.success({ title: response.data.message });
                    }, 1000);

                } else {
                    $('#countryadd').modal('hide');
                    toaster.clear();
                    toaster.error({ title: response.data.message });
                }
            }, function myError(err) {
                $scope.loader = false;
                console.log("Error response", err);
              });
    }
    $scope.closeOrCancel = function () {
       // $state.reload();
       $scope.country.countryName = "";
       $scope.country.countryCode = "";

    }
    $scope.updateCountry ;
    $scope.getUpdateCountry = function (update) {
        $scope.updateCountry = update;  
      }

    $scope.editCountry = function () {
        $scope.loader= true;

        var editcountrydata = { "countryId": $scope.updateCountry.countryId,"adminId": $scope.userProfileId, "countryName":  $scope.updateCountry.countryName, "countryCode":  $scope.updateCountry.countryCode };
        FunctionalityService.editCountry(editcountrydata)
            .then(function mySuccess(response) {
                $scope.loader= false;

                if (response.status == 201 || response.status == 200) {
                    $('#editCountry').modal('hide');
                    $state.reload();
                    $timeout(function () {
                        toaster.clear();
                        toaster.success({ title: response.data.message });
                    }, 1000);

                } else {
                    $('#editCountry').modal('hide');
                    toaster.clear();
                    toaster.error({ title: response.data.message });
                }
            }, function myError(err) {
                $scope.loader = false;
                console.log("Error response", err);
              });
    }
   var deleteCountry =[];
    $scope.getDeleteCountry = function (update) {
        deleteCountry.push(update.countryId);
      }
    $scope.deleteCountry = function () {
        $scope.loader= true;

        var deleteCountrydata = { "countryIds":  deleteCountry,"adminId": $scope.userProfileId };
        FunctionalityService.deleteCountry(deleteCountrydata)
        .then(function mySuccess(response) {
            $scope.loader= false;

            if (response.status == 201 || response.status == 200) {
                $('#delete').modal('hide');
                $state.reload();
                $timeout(function () {
                    toaster.clear();
                    toaster.success({ title: response.data.message });
                }, 1000);

            } else {
                $('#delete').modal('hide');
                toaster.clear();
                toaster.error({ title: response.data.message });
            }
        }, function myError(err) {
            $scope.loader = false;
            console.log("Error response", err);
          });
    }
    $scope.closeEditCountry = function () {
         $state.reload();
     }

     $scope.resetEditCountry = function() {
        $scope.editCountryForm.$setPristine();
        $scope.updateCountry.countryName="";
       $scope.updateCountry.countryCode="" ;
    };
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