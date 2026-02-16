var userMyWorkspace = angular.module('dapp.UserMyWorkSpceController', []);

userMyWorkspace.controller('UserMyWorkSpceController', ['$scope', '$window', '$location', '$state', '$rootScope', 'FunctionalityService', function ($scope, $window, $location, $state, $rootScope, FunctionalityService) {

    $scope.sessionObject = JSON.parse($window.localStorage.getItem('sessionObject'));

    $scope.groupVesselList = [];
    $scope.currentPage = 1;
    $scope.groupVesselListLength = $scope.groupVesselList.length;
    $scope.loader = false;
	$rootScope.selected = 3;
	
    //This method is used to get all ShipList
    $scope.getAllShipList = function () {
        $scope.loader = true;

        var data = {
            "userId": $scope.sessionObject.userId,
            "roleId": $scope.sessionObject.roleId
        };
        FunctionalityService.getVesselProfileList(data)
            .then(function (response) {
                $scope.loader = false;

                if (response.status == 200) {
                    $scope.groupVesselList = response.data.shipProfileList;
                    $scope.groupVesselListLength = $scope.groupVesselList.length;
                }
            }, function myError(err) {
                $scope.loader = false;
                console.log("Error response", err);
            });
    }

    //This used to redirect to grouo list page
    $scope.groupList = function (ship) {
        $window.localStorage.removeItem('groupShipId')
        $window.localStorage.setItem('groupShipId', ship.id);
        $window.localStorage.setItem('groupShipName', ship.shipName);
        $state.go('dapp.userMyWorkspaceList');
    }

}]);