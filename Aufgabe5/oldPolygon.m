close all
clear all
% Read polygon
Pol = dlmread('polygon.txt');
testPol = dlmread('testpolygon.txt');

% Check for vertical sides and avoid them by rotating all sides

% Plot polygon
%plot(polyshape(Pol))
%fill(Pol(:,1), Pol(:,2), 'k');
% fill(polSelfCoord(:,1), polSelfCoord(:,2), 'b');
% xlim([-0.1 10.1])
% ylim([-0.1 10.1])
% 
% % find 
% a = [2  4; 5 -1; -2 -4; -5 1];
% b = [-2; 28; 24; -6];
% 
% % Conditions
% sizePoll = size(polSelf);
% %b = ones(sizePoll(1), 1);
% %b = [-1; 27; 6; 6; 0; 0];
% [c,r] = chebycenter(a, b);
% % 
% hold on
% viscircles(c.',r);
% axis([-10 10 -10 10]) 
% hold off
% %x = linprog();

%Generate the input data
vertices = Pol;
%vertices = [0 0; 10 0; 10 10; 0 10];
%vertices = [ 1 1; 2 5; 5 4; 6 2; 4 1];
%vertices = [ 1 1; 6 0; 8 4; 3 5];
%edges = zip(vertices, vertices[1:] + [vertices[0]])
edges2 = vertices(2:size(vertices,1),:);
edges2 = [edges2; vertices(1,:)];

delta = edges2-vertices;

bibibi = delta(:,1).*vertices(:,2) - delta(:,2).*vertices(:,1);
a1 = [ 2;  4];
a2 = [ 5; -1];
a3 = [-2;  -4];
a4 = [-5; 1];
% a1 = [ 2;  1];
% a2 = [ 2; -1];
% a3 = [-1;  2];
% a4 = [-1; -2];
b = ones(4,1);
b(1) = -2;
b(2) = 28;
b(3) = 24;
b(4) = -6;

%figure()
% Create and solve the model
% cvx_begin
%     variable r(1)
%     variable x_c(2)
%     maximize ( r )
%     delta*x_c + r*norm(delta,2) <= bibibi;
% %     a1'*x_c + r*norm(a1,2) <= b(1);
% %     a2'*x_c + r*norm(a2,2) <= b(2);
% %     a3'*x_c + r*norm(a3,2) <= b(3);
% %     a4'*x_c + r*norm(a4,2) <= b(4);
% cvx_end

cvx_begin
    variable r(1)
    variable x_c(1)
    variable y(1)
    maximize ( r )
    %(delta(:,1)*y - delta(:,2)*x_c) + r*norm(delta,2) <= bibibi;
    (delta(:,1)*y - delta(:,2)*x_c) + (r * sqrt(delta(:,1).^2 + delta(:,2).^2)) <= bibibi;
%     a1'*x_c + r*norm(a1,2) <= b(1);
%     a2'*x_c + r*norm(a2,2) <= b(2);
%     a3'*x_c + r*norm(a3,2) <= b(3);
%     a4'*x_c + r*norm(a4,2) <= b(4);
cvx_end

% Generate the figure
x = linspace(-100,1200);
theta = 0:pi/100:2*pi;
hold on
%plot( x, -x*a1(1)./a1(2) + b(1)./a1(2),'b-');
%plot( x, -x*a2(1)./a2(2) + b(2)./a2(2),'b-');
%plot( x, -x*a3(1)./a3(2) + b(3)./a3(2),'b-');
%plot( x, -x*a4(1)./a4(2) + b(4)./a4(2),'b-');
plot(vertices(:,1),vertices(:,2),'*');

% optional: show lines defining half spaces
for i = 1:length(delta)
    plot( x, x*delta(i,2)./delta(i,1) + (bibibi(i,1)./delta(i,1)),'g');
end
% plot( x_c(1) + r*cos(theta), x_c(2) + r*sin(theta), 'r');
% plot(x_c(1),x_c(2),'k+')
plot( x_c + r*cos(theta), y + r*sin(theta), 'r');
plot(x_c,y,'k+')
plot(polyshape(Pol))

xlabel('x_1')
ylabel('x_2')
title('Largest Euclidean ball lying in a 2D polyhedron');
axis([0 1100 0 1100])
axis equal
