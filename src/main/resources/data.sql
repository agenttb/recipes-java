-- 国际化翻译测试数据

-- 产品名称翻译
INSERT INTO translation (message_key, locale, message_value, category) VALUES
('product.name.apple', 'zh-CN', '苹果', 'product'),
('product.name.apple', 'en-US', 'Apple', 'product'),
('product.name.apple', 'ja-JP', 'りんご', 'product'),
('product.name.banana', 'zh-CN', '香蕉', 'product'),
('product.name.banana', 'en-US', 'Banana', 'product'),
('product.name.banana', 'ja-JP', 'バナナ', 'product');

-- 产品描述翻译
INSERT INTO translation (message_key, locale, message_value, category) VALUES
('product.desc.apple', 'zh-CN', '新鲜美味的苹果', 'product'),
('product.desc.apple', 'en-US', 'Fresh and delicious apple', 'product'),
('product.desc.apple', 'ja-JP', '新鮮でおいしいりんご', 'product'),
('product.desc.banana', 'zh-CN', '进口优质香蕉', 'product'),
('product.desc.banana', 'en-US', 'Premium imported banana', 'product'),
('product.desc.banana', 'ja-JP', '輸入高品質バナナ', 'product');

-- 状态翻译
INSERT INTO translation (message_key, locale, message_value, category) VALUES
('status.active', 'zh-CN', '激活', 'status'),
('status.active', 'en-US', 'Active', 'status'),
('status.active', 'ja-JP', 'アクティブ', 'status'),
('status.inactive', 'zh-CN', '未激活', 'status'),
('status.inactive', 'en-US', 'Inactive', 'status'),
('status.inactive', 'ja-JP', '非アクティブ', 'status');

-- 分类翻译
INSERT INTO translation (message_key, locale, message_value, category) VALUES
('category.electronics', 'zh-CN', '电子产品', 'category'),
('category.electronics', 'en-US', 'Electronics', 'category'),
('category.electronics', 'ja-JP', '電子製品', 'category'),
('category.food', 'zh-CN', '食品', 'category'),
('category.food', 'en-US', 'Food', 'category'),
('category.food', 'ja-JP', '食品', 'category');

-- 标签翻译
INSERT INTO translation (message_key, locale, message_value, category) VALUES
('tag.hot', 'zh-CN', '热门', 'tag'),
('tag.hot', 'en-US', 'Hot', 'tag'),
('tag.hot', 'ja-JP', '人気', 'tag'),
('tag.new', 'zh-CN', '新品', 'tag'),
('tag.new', 'en-US', 'New', 'tag'),
('tag.new', 'ja-JP', '新着', 'tag'),
('tag.sale', 'zh-CN', '促销', 'tag'),
('tag.sale', 'en-US', 'Sale', 'tag'),
('tag.sale', 'ja-JP', 'セール', 'tag');
