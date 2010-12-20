#! /usr/bin/env ruby


from = ARGV[0]
to = ARGV[1]


files = Dir.glob(File.join(from, '*'))
i = 0
files.each do |file|
  split_file = file.split('/')
  file_name = split_file[split_file.length - 1]
  system("convert #{file} -resize 32x32\! #{File.join(to, file_name)}")
  i+= 1
end