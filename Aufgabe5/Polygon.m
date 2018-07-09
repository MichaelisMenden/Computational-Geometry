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
Pol = dlmread('polygon.txt');

%-----
% Get radius and center of max inner circle in polygon
%-----

vertices = Pol;
% next represent endpoints,
% for better access of start & end point by same index
next = vertices(2:size(vertices,1),:);
next = [next; vertices(1,:)];

% calculate slope of edge (A matrix)
delta = next - vertices;

% calculate b vector
b = delta(:,1).*vertices(:,2) - delta(:,2).*vertices(:,1);

%-----
% Get radius and center of max inner circle in polygon
%-----

[x, y, r] = chebyshevCenter(delta, b);

%-----
% Plot
%-----

% Generate the figure
x_l = linspace(-100,1200);
theta = 0:pi/100:2*pi;
hold on

% plot vertices
plot(vertices(:,1),vertices(:,2),'*');

% optional: show lines defining half spaces
for i = 1:length(delta)
    plot(x_l, x_l*delta(i,2)./delta(i,1) + (b(i,1)./delta(i,1)),'g');
end

% plot max inner circle 
plot( x + r*cos(theta), y + r*sin(theta), 'r');
% plot center point
plot(x,y,'k+')
% plot polygon
plot(polyshape(vertices))

% some config for figure
xlabel('x_1')
ylabel('x_2')
title('maximum radius inscribed circle in a 2D polygon');
axis([0 1100 0 1100])
axis equal
