//
//  UserCell.m
//  ZHEvent
//
//  Created by jack on 8/29/15.
//  Copyright (c) 2015 jack. All rights reserved.
//

#import "FanCell.h"
#import "SSUser.h"
#import "UIButton+Color.h"


@interface FanCell ()
{
    
    UILabel *_nameL;
    UILabel *_metaL;
    UIImageView *_actorLogo;
 
    UILabel *_line;
    
    UIButton *_btnAdd;
}

@end

@implementation FanCell
@synthesize _btnAdd;

- (id) initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    
    if(self = [super initWithStyle:style reuseIdentifier:reuseIdentifier])
    {
        
        self.backgroundColor = [UIColor clearColor];
        
        
        _actorLogo = [[UIImageView alloc] initWithFrame:CGRectMake(10, 10, 50, 50)];
        _actorLogo.layer.cornerRadius = 25;
        _actorLogo.clipsToBounds = YES;
        _actorLogo.backgroundColor = [UIColor clearColor];
        _actorLogo.layer.contentsGravity = kCAGravityResizeAspectFill;
        [self.contentView addSubview:_actorLogo];

        
        _nameL = [[UILabel alloc] initWithFrame:CGRectMake(70,
                                                           15,
                                                           SCREEN_WIDTH-100, 20)];
        _nameL.backgroundColor = [UIColor clearColor];
        [self.contentView addSubview:_nameL];
        _nameL.font = [UIFont boldSystemFontOfSize:15];
        _nameL.textAlignment = NSTextAlignmentLeft;
        _nameL.textColor  = [UIColor blackColor];
        _nameL.text = @"";
        
        _metaL = [[UILabel alloc] initWithFrame:CGRectMake(70,
                                                           35,
                                                           SCREEN_WIDTH-150, 20)];
        _metaL.backgroundColor = [UIColor clearColor];
        [self.contentView addSubview:_metaL];
        _metaL.font = [UIFont systemFontOfSize:13];
        _metaL.textAlignment = NSTextAlignmentLeft;
        _metaL.textColor  = COLOR_TEXT_A;
        _metaL.text = @"";
        
        
        _line = [[UILabel alloc] initWithFrame:CGRectMake(0, 69, SCREEN_WIDTH, 1)];
        _line.backgroundColor = LINE_COLOR;
        [self.contentView addSubview:_line];
        
        
        _btnAdd = [UIButton buttonWithColor:[UIColor whiteColor] selColor:nil];
        _btnAdd.layer.borderColor = THEME_RED_COLOR.CGColor;
        _btnAdd.layer.borderWidth = 1;
        _btnAdd.frame = CGRectMake(SCREEN_WIDTH-75, 25, 65, 30);
        [self.contentView addSubview:_btnAdd];
        [_btnAdd setTitle:@"加好友" forState:UIControlStateNormal];
        [_btnAdd setTitleColor:THEME_RED_COLOR forState:UIControlStateNormal];
        _btnAdd.titleLabel.font = [UIFont systemFontOfSize:14];
        _btnAdd.layer.cornerRadius = 3;
        _btnAdd.clipsToBounds = YES;
        _btnAdd.hidden = YES;
        
    }
    
    return self;
}

- (void)awakeFromNib {
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (void) fillData:(SSUser*) person{
    
    _nameL.text = person.fullname;
    
    if([person.companyname length])
    {
        _metaL.text = person.companyname;
        
        if(person.ranktitle)
        {
             _metaL.text = [NSString stringWithFormat:@"%@ %@",person.companyname, person.ranktitle];
        }
    }
    else
    {
        if(person.ranktitle)
        {
            _metaL.text = person.ranktitle;
        }
    }
    
    NSString *avatarUrl = person.avatarurl;
    if(avatarUrl)
    {
        [_actorLogo setImageWithURL:[NSURL URLWithString:avatarUrl] placeholderImage:[UIImage imageNamed:@"default_avatar.png"]];
        
    }
    else
    {
        [_actorLogo setImage:[UIImage imageNamed:@"default_avatar.png"]];
    }
    
    
}

@end
