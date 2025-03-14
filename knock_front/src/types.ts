export interface ICategory {
  categoryId: string;
  categoryNm: string;
  movies: string[];
}

export interface ISearch {
  searchTitle: string;
  searchCategory: string;
}

export interface ICategoryLevelTwo {
  id: string;
  nm: string;
  parentNm: string;
  favoriteUsers: string[];
}

export interface IMovie {
  movieId: string;
  movieNm: string;
  openingTime: string;
  reservationLink: string[];
  posterBase64: string;
  directors: string[];
  actors: string[];
  companyNm: string[];
  categoryLevelTwo: ICategoryLevelTwo[];
  runningTime: number;
  plot: string;
  favorites: string[];
  koficcode: string;
}

export interface ISubList {
  MOVIE: IMovie[];
  PERFORMING_ARTS: any;
}

export interface IUser {
  alarmTimings: string[];
  email: string;
  favoriteLevelOne: 'MOVIE' | 'PERFORMING_ARTS';
  id: string;
  lastLoginTime: string;
  loginType: 'KAKAO' | 'GOOGLE' | 'NAVER' | 'GUEST';
  name: string;
  nickName: string;
  picture: string;
  role: string;
  subscribeList: {
    MOVIE: IMovie[];
    PERFORMING_ARTS: any;
    EXHIBITION: any;
  };
}
