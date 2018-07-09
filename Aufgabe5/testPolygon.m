close all
clear all

%-----
% BEFORE STARTING
% If you didn't run cvx_setup.m until last start of Matlab:
% please execute 'run cvx_setup' in matlab command line
%-----

%-----
% Read data from .txt-File
%-----

% Read polygon
testPol = dlmread('testpolygon.txt');

%-----
% Get radius and center of max inner circle in polygon
%-----

test_vertices = testPol;
% next represent endpoints,
% for better access of start & end point by same index
test_next = test_vertices(2:size(test_vertices,1),:);
test_next = [test_next; test_vertices(1,:)];

% calculate slope of edge (A matrix)
test_delta = test_next - test_vertices;

% calculate b vector
test_b = test_delta(:,1).*test_vertices(:,2) - test_delta(:,2).*test_vertices(:,1);

%-----
% Get radius and center of max inner circle in polygon
%-----

[test_x, test_y, test_r] = chebyshevCenter(test_delta, test_b);

%-----
% Plot
%-----

% Generate the figure
axis([0 30 -10 30])
axis equal
x_l = linspace(-10,30);
theta = 0:pi/100:2*pi;
hold on

% plot vertices
plot(test_vertices(:,1),test_vertices(:,2),'*');

% optional: show lines defining half spaces
for i = 1:length(test_delta)
    if test_delta(i,1) == 0
        plot([test_vertices(i,1) test_vertices(i,1)],ylim, 'g'); 
    else 
        plot(x_l, x_l*test_delta(i,2)./test_delta(i,1) + (test_b(i,1)./test_delta(i,1)),'g');
    end
end

% plot max inner circle 
plot( test_x + test_r*cos(theta), test_y + test_r*sin(theta), 'r');
% plot center point
plot(test_x,test_y,'k+')
% plot polygon
plot(polyshape(test_vertices))

% some config for figure
xlabel('x_1')
ylabel('x_2')
title('maximum radius inscribed circle in test polygon');

