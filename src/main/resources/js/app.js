/**
 * Created by bdonnell on 18/06/16.
 */
var module = angular.module("flybuys", ['ngSanitize', 'ngRoute', 'ui.bootstrap']);

module.config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when('/home', {
        controller: 'homeCtrl',
        templateUrl: 'html/home.html'
    })
        .when('/gamble/:userId', {
            controller: 'gambleCtrl',
            templateUrl: 'html/gamble.html',
            resolve: {
                points: ['$http', '$q', '$route', function ($http, $q, $route) {
                    var deferral = $q.defer();
                    var userId = $route.current.params.userId;
                    $http.get('/rest/flybuys/points/' + userId)
                        .success(function (data) {
                            deferral.resolve(data[userId]);
                        }).error(function () {
                        deferral.reject("Cannot get points for user.")
                    });
                    return deferral.promise;
                }]
            }
        })
        .otherwise({redirectTo: '/home'})
}]);

module.controller('homeCtrl', ['$scope', '$http', '$location', function ($scope, $http, $location) {
    $scope.submitForm = function () {
        $http({
            method: 'POST',
            url: '/rest/flybuys/validatecard/' + $scope.formData.cardnum
        }).success(function (data) {
            $location.path('/gamble/' + data.userId);
        }).error(function () {
            $scope.formData.cardnum = '';
            $scope.error = "Cannot validate card number.";
        })
    };
    $scope.error = '';
    $scope.formData = {
        cardnum: ''
    }
}]);

module.controller('gambleCtrl', ['$scope', '$http', '$location', '$route', 'points', function ($scope, $http, $location, $route, points) {
    $scope.points = points;
    $scope.formData = {
        points: 10
    };
    $scope.messageClass = 'alert-success';
    $scope.message = '';
    $scope.rolled = null;
    $scope.username = $route.current.params.userId;
    function win(points) {
        $http({
            method: 'PUT',
            url: '/rest/flybuys/points/' + $scope.username + '/' + points
        }).success(function (data) {
            $scope.points = data[$scope.username];
            $scope.message = "You won " + points + " points!<br/>Congratulations!"
            $scope.messageClass = 'alert-success';
        }).error(function () {
            $scope.error = "We could not update you points."
        })
    }

    function lose(points) {
        $http({
            method: 'DELETE',
            url: '/rest/flybuys/points/' + $scope.username + '/' + points
        }).success(function (data) {
            $scope.points = data[$scope.username];
            $scope.message = "You lost " + points + " points!<br/>Better luck next time!"
            $scope.messageClass = 'alert-warning';
        }).error(function () {
            $scope.error = "We could not update you points."
        })
    }

    $scope.gamblePoints = function () {
        var gambling = $scope.formData.points;
        var rnd = Math.floor((Math.random() * 100) + 1);
        $scope.rolled = rnd;
        var mod = rnd % 2;
        if (mod == 0) {
            win(gambling);
        }
        else {
            lose(gambling);
        }
    }
}]);