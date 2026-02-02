var manageSubScription = angular.module('dapp.SaManageSubscriptionController', ['angularUtils.directives.dirPagination', 'toaster', 'moment-picker', '720kb.tooltips']);

manageSubScription.controller('SaManageSubscriptionController', ['$scope', '$window', '$location', '$state', '$rootScope', '$timeout', 'toaster', 'FunctionalityService', function ($scope, $window, $location, $state, $rootScope, $timeout, toaster, FunctionalityService) {
    $scope.currentPage = 1;
    $scope.viewby = 10;
    $scope.itemsPerPage = $scope.viewby;
    $scope.allSubscriptionList;
    $scope.loader= false;

    $scope.sessionObject = JSON.parse($window.localStorage.getItem('sessionObject'));

    $scope.mindate = moment().add(0, 'day');
    $scope.expandSubMenu = function () {
        if ($rootScope.subMenuActive == true) {
            $rootScope.subMenuActive = false;
        }
        else {
            $rootScope.subMenuActive = true;
            $rootScope.subConfigMenuActive = false
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
    $scope.mangeAdminSubscription = function (subscription) {
        $state.go("dapp.saManageSubscriptionAdmin", { "userId": $scope.sessionObject.userId, "organizationId": subscription.organizationId, "subscriptionId": subscription.subscriptionId });
    }
    $scope.subscriptionList = function () {
        $scope.loader= true;

        FunctionalityService.getSubscriptionList($scope.sessionObject.userId)
            .then(function mySuccess(response) {
                $scope.loader= false;

                if (response.status == 201 || response.status == 200) {

                    $scope.allSubscriptionList = JSON.stringify(response.data.subscriptionInfos);
                    $scope.allSubscriptionList = response.data.subscriptionInfos;
                    if ($scope.allSubscriptionList == undefined) {
                        toaster.info({ title: "No records found" });
                    }
                }
            }, function myError(err) {
                $scope.loader = false;
                console.log("Error response", err);
              });
    }
    $scope.viewSubscrition = function (sub) {
        $scope.viewsub = sub;
    }
    $scope.editSub;
    $scope.editSubscrition = function (sub) {
        $scope.editSub = sub;
        console.log("editSubscrition", JSON.stringify($scope.editSub));
    }
    $scope.close = function () {
        $('#subscriptview').modal('hide');
        $state.reload();
    }
    // $scope.clear = function () {
    //     $scope.editSub.organizationName="";
    //     $scope.editSub.registerNumber="";
    //     $scope.editSub.shipmentCount="";
    //     $scope.editSub.userCount="";
    // }

    $scope.editSubscription = function () {
        $scope.loader= true;
        var createOrganizationData = {
            "userId":$scope.sessionObject.userId,
            "subscriptionId":  $scope.editSub.subscriptionId,
            "shipmentCount":  $scope.editSub.shipmentCount,
            "userVesslesRatio":  $scope.editSub.userVesslesRatio,
            "fromDate": $scope.editSub.fromDate,
            "toDate": $scope.editSub.toDate,
            "isStatusAlive":1
        };
        FunctionalityService.editAndUpdateSubscription(createOrganizationData)
            .then(function mySuccess(response) {
                $scope.loader= false;
                if (response.status == 201 || response.status == 200) {
                    $('#subscriptedit').modal('hide');
                    $state.reload();
                    $timeout(function () {
                        toaster.clear();
                        toaster.success({ title: response.data.message });
                    }, 1000);
                    //$state.go('dapp.saManageSubscription');

                } else {
                    $('#subscriptedit').modal('hide');
                    toaster.clear();
                    toaster.error({ title: response.data.message });
                }
            }, function myError(err) {
                $scope.loader = false;
                console.log("Error response", err);
              });
    }
    $scope.resetsubEdit = function() {
        $scope.updateSubForm.$setPristine();
        $scope.editSub.shipmentCount="";
        $scope.editSub.userVesslesRatio="";
        $scope.editSub.fromDate="Start Date";
        $scope.editSub.toDate="End Date";

    };
    $scope.cancelSubEdit = function() {
        $('#subscriptedit').modal('hide');
        $state.reload();
    }
    
  
}]);