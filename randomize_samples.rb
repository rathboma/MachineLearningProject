#! /usr/bin/env ruby
require 'rubygems'
require 'fileutils'

all = './small_covers'
training = './small_covers_training'
test = './small_covers_test'
mappings = File.open('mapping.txt').readlines
allowed_files = mappings.map{|f| f.split(/\s+/)[0].strip.downcase}


system("rm -rf #{File.join(training, "*")}")
system("rm -rf #{File.join(test, "*")}")
system("cp #{File.join(all, '*')} #{training}")

files = Dir.glob(File.join(training, '*'))

#get rid of files without a mapping
files.each do |file|
  split = file.split('/')
  file_name = split[split.size- 1]
  unless allowed_files.include?(file_name.strip.downcase)
    system("rm #{file}")
  end
end


(0..49).each do |i|
  file = files[rand(files.size)]
  
  files.delete(file)
  system("mv #{file} #{test}")
end