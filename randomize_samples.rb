#! /usr/bin/env ruby
require 'rubygems'
require 'fileutils'

all = './small_covers'
training = './small_covers_training'
test = './small_covers_test'

system("rm -rf #{File.join(training, "*")}")
system("rm -rf #{File.join(training, "*")}")


files = Dir.glob(File.join(all, '*'))

system("cp #{File.join(all, '*')} #{training}")
files = Dir.glob(File.join(training, '*'))
(0..49).each do |i|
  puts i
  file = files[rand(files.size)]
  files.delete(file)
  system("mv #{file} #{test}")
end