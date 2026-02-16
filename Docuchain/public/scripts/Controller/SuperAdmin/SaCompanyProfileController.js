var companyProfile = angular.module('dapp.SaCompanyProfileController', ['angularUtils.directives.dirPagination', 'toaster',, 'moment-picker', '720kb.tooltips']);

companyProfile.controller('SaCompanyProfileController', ['$scope', '$stateParams', '$window', '$location', '$state', '$rootScope', '$timeout', 'toaster', 'FunctionalityService', function ($scope, $stateParams, $window, $location, $state, $rootScope, $timeout, toaster, FunctionalityService) {
    // $scope.tabName = tab1;
    $scope.currentPage = 1;
    $scope.viewby = 10;
    $scope.itemsPerPage = $scope.viewby;
    $scope.sessionObject = JSON.parse($window.localStorage.getItem('sessionObject'));

    $scope.allOrganizationList;
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
            $rootScope.subMenuActive = false
        }

    }

    $scope.organizationList = function () {
        $scope.loader= true;

        FunctionalityService.getOrganizationList($scope.sessionObject.userId)
            .then(function mySuccess(response) {
                $scope.loader= false;

                if (response.status == 201 || response.status == 200) {

                    $scope.allOrganizationList = JSON.stringify(response.data.organizationInfos);
                    $scope.allOrganizationList = response.data.organizationInfos;
                    if ($scope.allOrganizationList == undefined) {
                        toaster.pop('info', "No records found");
                    }
                }
            }, function myError(err) {
                $scope.loader = false;
                console.log("Error response", err);
            });
    }

    $scope.changeStatusOrganization = function (organization) {
        $scope.loader= true;

        $scope.status;
        if (organization.isStatusActive == 1) {
            $scope.status = 0;
        } else {
            $scope.status = 1;
        }
        var changeStatusOrganization = { "organizationId": organization.organizationId, "userId": $scope.sessionObject.userId, "isStatusActive": $scope.status };
        FunctionalityService.changeStatusOrganization(changeStatusOrganization)
            .then(function mySuccess(response) {
                $scope.loader= false;

                if (response.status == 201 || response.status == 200) {
                toaster.clear();
                toaster.success({ title: response.data.message });
                $state.reload();

            }else {
                toaster.clear();
                toaster.error({ title: response.data.message });
            }
            }, function myError(err) {
                $scope.loader = false;
                console.log("Error response", err);
              });
    }
    $scope.changeOrgDualApprovel = function (organization) {
        $scope.loader= true;

        $scope.isDualApprovalstatus;
        if (organization.isDualApprovalActive == 1) {
            $scope.isDualApprovalstatus = 0;
        } else {
            $scope.isDualApprovalstatus = 1;
        }
        var changeStatusOrganization = { "organizationId": organization.organizationId, "userId": $scope.sessionObject.userId, "isDualApprovalActive": $scope.isDualApprovalstatus };
        FunctionalityService.changeOrgDualApprovel(changeStatusOrganization)
            .then(function mySuccess(response) {
                $scope.loader= false;

                if (response.status == 201 || response.status == 200) {
                    toaster.clear();
                    toaster.success({ title: response.data.message });
                }else {
                    toaster.clear();
                    toaster.error({ title: response.data.message });
                }
            }, function myError(err) {
                $scope.loader = false;
                console.log("Error response", err);
            });
    }

    $scope.changeSaveInBlockchain = function (organization) {
        $scope.loader = true;

        var changeStatusOrganization = {
            "organizationId": organization.organizationId,
            "userId": $scope.sessionObject.userId,
            "isSaveInBlockchain": (organization.isSaveInBlockchain == 1) ? 0 : 1
        };

        FunctionalityService.changeSaveInBlockchain(changeStatusOrganization)
            .then(function mySuccess(response) {
                $scope.loader = false;
                toaster.clear();
                if (response.status == 201 || response.status == 200) {
                    toaster.success({
                        title: response.data.message
                    });
                } else {
                    toaster.error({
                        title: response.data.message
                    });
                }
                $state.reload();
            }, function myError(err) {
                $scope.loader = false;
                console.log("Error response", err);
                $state.reload();
            });
    }

    $scope.delete;
    $scope.getDeleteId = function (organization) {
        $scope.delete = organization.organizationId;
    }

    $scope.updateOrganizationId;
    $scope.getUpdateId = function (organization) {
        $scope.updateOrganizationId = organization.organizationId;
        $state.go("dapp.saCompanyProfileupdate", { "userId": $scope.sessionObject.userId, "organizationId": organization.organizationId });
    }
    $scope.viewOrganization;
    $scope.viewOrganizationById = function (organization) {
        $scope.viewOrganization = organization.organizationId;
        $state.go("dapp.saCompanyProfileView", { "organizationId": organization.organizationId });
    }

    $scope.deleteOrganization = function () {
        $scope.loader= true;

        var deleteOrganizationdata = { "userId": $scope.sessionObject.userId, "organizationId": $scope.delete };
        FunctionalityService.deleteOrganization(deleteOrganizationdata)
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
                    toaster.clear();
                    toaster.error({ title: response.data.message });
                }
            }, function myError(err) {
                $scope.loader = false;
                console.log("Error response", err);
            });
    }
}]);